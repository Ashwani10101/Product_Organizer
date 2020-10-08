package com.ash.shopadminlogin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.ThumbnailUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ash.shopadminlogin.database.ProductEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddProductActivity : AppCompatActivity() {


    private lateinit var btnDeleteCategory    : Button
    private lateinit var btnNewCategory       : Button
    private lateinit var editProductName      : EditText
    private lateinit var editProductPrice     : EditText
    private lateinit var editProductDetails   : EditText
    private lateinit var btnAddFromGallery    : Button
    private lateinit var btnRemoveImage       : Button
    private lateinit var imageViewProduct     : ImageView
    private lateinit var spinnerCategory      : Spinner
    private lateinit var btnCancel            : Button
    private lateinit var btnNewSave           : Button


    private var productImage : Bitmap? = null


    //Access Codes
    private val READ_EXTERNAL_STORAGE_PERMISSION = 200
    private val PICK_FROM_GALLERY = 201


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sub_activity_add_product)

        initLayout()
        initSpinner()

    }

    private fun initLayout()
    {
        btnDeleteCategory  = findViewById(R.id.addproduct_btn_deleteCategory)
        btnNewCategory     = findViewById(R.id.addproduct_btn_newCategory)
        editProductName    = findViewById(R.id.addproduct_edit_productName)
        editProductPrice   = findViewById(R.id.addproduct_edit_productPrice)
        editProductDetails = findViewById(R.id.addproduct_edit_productDetails)
        btnAddFromGallery  = findViewById(R.id.addproduct_btn_addFromGallery)
        btnRemoveImage     = findViewById(R.id.addproduct_btn_removeImage)
        imageViewProduct   = findViewById(R.id.addproduct_image_productImage)
        spinnerCategory    = findViewById(R.id.addproduct_spinner_Category)
        btnCancel          = findViewById(R.id.addproduct_btn_cancel)
        btnNewSave         = findViewById(R.id.addproduct_btn_Save)


        btnAddFromGallery.setOnClickListener {
            requestImage()
        }
        btnRemoveImage.setOnClickListener {
            imageViewProduct.setImageResource(R.drawable.no_image_selected)
        }

        btnNewSave.setOnClickListener {
           saveProduct()

        }

        btnCancel.setOnClickListener {
            finish()
        }


    }

    private fun saveProduct() {


        val productEntity = ProductEntity()
        productEntity.name = editProductName.text.toString()
        productEntity.price = editProductPrice.text.toString()
        productEntity.details = editProductDetails.text.toString()
        productEntity.category = spinnerCategory.selectedItem.toString()
        productEntity.imageID =  saveImage()

        intent.putExtra("PRODUCT",productEntity)
        setResult(Activity.RESULT_OK, intent)

        finish()

    }



    private fun saveImage() : String
    {

        val uniqueID = getNewDateStamp()
        val imagesDir = File(filesDir, "Images")
        if (!imagesDir.exists()) {imagesDir.mkdir()}


        val file = File(imagesDir,"$uniqueID.jpg")
        val fos  = FileOutputStream(file)


        val scaledProductImage = ThumbnailUtils.extractThumbnail(productImage!!, 120*2, 150*2)
        scaledProductImage.compress(Bitmap.CompressFormat.JPEG,90,fos)
        fos.flush()

        return uniqueID
    }

    fun getNewDateStamp() : String
    {
        //Time stamp
        val sdf = SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS", Locale.getDefault())
        return sdf.format(Date())
    }


    private fun initSpinner()
    {

        val arrayData: ArrayList<String> = ArrayList()


        val list = MainActivity.ALL_CATEGORYS.split(',')
        arrayData.addAll(list)

        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            arrayData
        )
        {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return setCentered(super.getView(position, convertView, parent))!!
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                return setCentered(super.getDropDownView(position, convertView, parent))!!
            }

            private fun setCentered(view: View): View? {
                val textView = view.findViewById<View>(android.R.id.text1) as TextView
                textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                textView.isAllCaps = true


                return view
            }
        }


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode)
        {
            PICK_FROM_GALLERY -> {
                if(resultCode == Activity.RESULT_OK) { gotResultImage(data) }
            }
        }
    }


    /**---------------Pick image from gallery----------------*/
    private fun requestImage()
    {
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, READ_EXTERNAL_STORAGE_PERMISSION)
        } else
        {
            pickImageFromGallery()
        }

    }

    private fun pickImageFromGallery()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_FROM_GALLERY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {   super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode)
        {
            //100 ->
            READ_EXTERNAL_STORAGE_PERMISSION ->
            {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    pickImageFromGallery()
                }
            }
        }
    }


    private fun gotResultImage(data: Intent?)
    {
       val imageUri= data?.data

        if(imageUri!=null)
        {
            val source = ImageDecoder.createSource(this.contentResolver,imageUri)
            productImage = ImageDecoder.decodeBitmap(source)
            imageViewProduct.setImageBitmap(productImage)
        } else
        {
            showToast("NO Image Selected")
        }


    }

    private fun showToast(msg:String)
    {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    private fun show(message:String)
    {
        Log.i("###",message)
    }



}
