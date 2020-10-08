package com.ash.shopadminlogin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.ash.shopadminlogin.database.MyDatabase
import com.ash.shopadminlogin.database.ProductEntity
import com.ash.shopadminlogin.adaptors.MainRecycleViewAdaptor
import com.facebook.stetho.Stetho
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity()
{

    private lateinit var recyclerviewAdaptor: MainRecycleViewAdaptor
    private lateinit var spinner: Spinner
    private lateinit var searchView: SearchView //SearchView query needed by fragment tab change listener
    private val sharedPrefFile = "kotlinsharedpreference"

    companion object
    {
        val ALL_CATEGORYS = "-ALL-,ATTA,MASALE,GRAINS,PULSES,RICE,DRY FRUITS,SWEETNERS,SATTO,OTHERS"

        val ADD_NEW_PRODUCT = 100

        var myDatabase: MyDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setTheme(R.style.MainPageTheme) //Setting splash screen
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_MainActivity)
        Stetho.initializeWithDefaults(applicationContext) //Database Browser

        initDrawer()
        initDatabase()
        initSpinner()
        initRecycleView()
        initSharePreferences()

    }

    private fun initSharePreferences()
    {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val isFirst = sharedPreferences.getBoolean("isFirstTimeInstall", true)

        //First time App running
        if (isFirst)
        {
            Handler().postDelayed({
                refresh()
            }, 1000)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("isFirstTimeInstall", false)
            editor.apply()
            editor.commit()
        } else
        {

        }

    }

    private lateinit var drawer: DrawerLayout
    private fun initDrawer()
    {
        drawer = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar_MainActivity, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        mainActivity_textviewDeliverys.setOnClickListener {
            val intent = Intent(this,CustomerActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initDatabase()
    {
        myDatabase = Room.databaseBuilder(applicationContext, MyDatabase::class.java, "ProductDatabase").build()
    }

    private fun initRecycleView()
    {
        recyclerviewAdaptor = MainRecycleViewAdaptor()

        reycleviewMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reycleviewMain.adapter = recyclerviewAdaptor


        //Loading the recycleView
        CoroutineScope(Dispatchers.IO).launch {
            val productListWithoutImages = ArrayList(myDatabase!!.productDao().products())


            val productList = ArrayList<ProductEntity>()

            for (p in productListWithoutImages)
            {
                show("Getting Imagage ID = ${p.imageID.toString()}")
                p.image = Helper.getThumbnail(applicationContext, p.imageID.toString())
                if (p.image == null)
                {
                    p.image = getColorBitmap()
                }
                productList.add(p)
                show("Image = ${p.image}")
            }
            recyclerviewAdaptor.addProductList(productList)
        }

    }

    private fun initSpinner()
    {
        spinner = findViewById(R.id.main_spinnerCategorySelector)

        val spinnerData: ArrayList<String> = ArrayList()


        val list = ALL_CATEGORYS.split(',')
        spinnerData.addAll(list)

        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_dropdown_item, spinnerData)
        {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
            {
                return setCentered(super.getView(position, convertView, parent))!!
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View
            {
                return setCentered(super.getDropDownView(position, convertView, parent))!!
            }

            private fun setCentered(view: View): View?
            {
                val textView = view.findViewById<View>(android.R.id.text1) as TextView
                textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                textView.isAllCaps = true


                return view
            }
        }


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //Lister
        spinner.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long)
            {
                val name = selectedItemView.findViewById<TextView>(android.R.id.text1).text.toString()
                show("Spinner Selected : $name")
                if (name == "-ALL-")
                {

                    recyclerviewAdaptor.filter.filter("")
                } else
                {
                    //   var str = recyclerviewAdaptor.filter
                    recyclerviewAdaptor.filter.filter(name)
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>?)
            {
                recyclerviewAdaptor.filter.filter("")
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode)
        {
            ADD_NEW_PRODUCT ->
            {

                //User
                val productEntity: ProductEntity? = data?.getParcelableExtra("PRODUCT")
                if (productEntity != null)
                {
                    productEntity.image = Helper.getThumbnail(applicationContext, productEntity.imageID.toString())

                    recyclerviewAdaptor.addProduct(productEntity, recyclerviewAdaptor.itemCount)
                    CoroutineScope(Dispatchers.IO).launch {
                        myDatabase!!.productDao().addNewProduct(productEntity)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity_toolbar, menu)
        val item = menu!!.findItem(R.id.MainMenuSearchButton)
        searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean
            {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean
            {   //Coroutine added
                if (query.toString().isNotEmpty() || query.toString() != "")
                {
                    showToast(query.toString())
                    recyclerviewAdaptor.filter.filter(query)
                } else
                {
                    recyclerviewAdaptor.filter.filter(spinner.selectedItem.toString())
                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {


        return when (item.itemId)
        {
            R.id.MainMenueAddCategory ->
            {
                val intent = Intent(this, AddProductActivity::class.java)
                startActivityForResult(intent, ADD_NEW_PRODUCT)
                return true
            }
            R.id.MainMenuRefresh ->
            {

                showToast("Auto Loader Tool Loaded Data!")
                refresh()
                return true
            }

            R.id.MainMenuClearAll ->
            {

                showToast("All Clear")
                recyclerviewAdaptor.clear()
                CoroutineScope(Dispatchers.IO).launch {
                    myDatabase!!.productDao().deleteAll()
                }


                return true
            }
            R.id.MainMenuSORT ->
            {

                showToast("Sort Working!!")
                return true
            }
            R.id.MainMenuSearchButton ->
            {

                showToast("Search Clicked!!!")
                return true
            }

            else -> super.onOptionsItemSelected(item)


        }


    }

    private fun refresh()
    {

        // val data = "M.P SHARBATI DELUXE ATTA ; ATTA ; 40 ; No Details@MP SHARBATI ATTA ; ATTA ; 38 ; No Details@HARYANA DESI ATTA ; ATTA ; 30 ; No Details@NORMAL FRESH ATTA ; ATTA ; 24 ; No Details@MULTI GRAIN ATTA ; ATTA ; 50 ; No Details@JO ATTA (BARLEY) ; ATTA ; 80 ; No Details@JWAR ATM CHANA ATTA SOYABEAN ATTA ; ATTA ; 80 ; No Details@MAKKI ATTA ; ATTA ; 40 ; No Details@BAJRA ATTA ; ATTA ; 30 ; No Details@RICE POWDER ; ATTA ; 40 ; No Details@URAD DAL ATTA ; ATTA ; 140 ; No Details@MOONS DAL ATTA ; ATTA ; 130 ; No Details@RAGI ATTA ; ATTA ; 80 ; No Details@BESAN ; ATTA ; 90 ; No Details@BESAN (MOTA) ; ATTA ; 90 ; No Details@MAIDA ; ATTA ; 40 ; No Details@SUJI ; ATTA ; 40 ; No Details@DALIA ; ATTA ; 40 ; No Details@OATS ; ATTA ; 40 ; No Details@ARHAR (TOOK) ; PULSES ; 79 ; No Details@MOONS DHULI ; PULSES ; 82 ; No Details@MOONS CHILKA ; PULSES ; 80 ; No Details@MOONS SABUT ; PULSES ; 78 ; No Details@URAD DHULI ; PULSES ; 110 ; No Details@URAD CHILKA ; PULSES ; 87 ; No Details@SABUT MASUR BLACK ; PULSES ; 43 ; No Details@MASUR MALKA ; PULSES ; 53 ; No Details@RAJMA CHITRA, JAMMU ; PULSES ; 52 ; No Details@CHANA DAL ; PULSES ; 53 ; No Details@MIX DAL ; PULSES ; 86 ; No Details@MOTH DAL ; PULSES ; 24 ; No Details@LAL LOBIA, SAFED LOBIA ; PULSES ; 245 ; No Details@MATAR ; PULSES ; 23 ; No Details@MASUR DALLI ; PULSES ; 26 ; No Details@KALA CHANA ; PULSES ; 73 ; No Details@BULI CHANA ; PULSES ; 73 ; No Details@SABUT GARAM MASALA MIND MOTO ; MASALA ; 78 ; No Details@JEERA ; MASALA ; 75 ; No Details@LONG ; MASALA ; 783 ; No Details@KALANOJI ; MASALA ; 34 ; No Details@JAI FAL,JAVITRI ; MASALA ; 43 ; No Details@SUPARI ; MASALA ; 65 ; No Details@TEJ PATTA , KADI PATTA ; MASALA ; 756 ; No Details@KALI MIRCH SABUT ; MASALA ; 768 ; No Details@HALDI, DHANIA, AMCHOOR ; MASALA ; 54 ; No Details@LAL MIRCH SABUT ; MASALA ; 44 ; No Details@SARSO YELLOW, BLACK ; MASALA ; 43 ; No Details@RAI, METH' DANA ; MASALA ; 43 ; No Details@SAUNF MOT', BAREEK ; MASALA ; 53 ; No Details@ORIGANOS ; MASALA ; 36 ; No Details@IMLI,SAUNTH,GOOND ; MASALA ; 63 ; No Details@EXTRA LONG GRAIN 1121 XXXL BASMATI ROYAL RICE ; RICE ; 57 ; No Details@LONG GRAIN 1121 XXL BASMATI DELIGHT RICE ; RICE ; 57 ; No Details@PREMIUM 1121 XL BASMATI RICE ; RICE ; 57 ; No Details@ROZANA BASMATI RICE ; RICE ; 57 ; No Details@BASMATI TIBAR(1121) ; RICE ; 57 ; No Details@BASMATI DUBAR (1121) ; RICE ; 57 ; No Details@SAUNA MASORI RICE ; RICE ; 57 ; No Details@PARMAL DOUBLE SILKY RICE ; RICE ; 57 ; No Details@GOLDEN SELLA XXXL 1121 ; RICE ; 57 ; No Details@GOLDEN SELLA XXL 1121 ; RICE ; 57 ; No Details@BASMATI SUPER MOGRA ; RICE ; 57 ; No Details@IDLI RICE ; RICE ; 57 ; No Details@BROWN RICE ; RICE ; 57 ; No Details@KAJU ; DRY FRUITS ; 76 ; No Details@BADAM ; DRY FRUITS ; 76 ; No Details@PISTA,GREEN PISTA ; DRY FRUITS ; 76 ; No Details@AKROT GIRT, SABUT ; DRY FRUITS ; 76 ; No Details@KHUMANI ANJEER ; DRY FRUITS ; 76 ; No Details@KHARBUJ MAGAJ , TARBOJ MAGAJ ; DRY FRUITS ; 76 ; No Details@CHUWARA ; DRY FRUITS ; 76 ; No Details@GOLA, GOLA POWDER ; DRY FRUITS ; 76 ; No Details@KHASKHAS ; DRY FRUITS ; 76 ; No Details@BAJRA JWAR; GRAINS ; 89 ; No Details@MAKKA .10(BARLEY) ; GRAINS ; 89 ; No Details@RAGI ; GRAINS ; 89 ; No Details@FLEX SEEDS(ALSI) ; GRAINS ; 89 ; No Details@SATANAJ MIX ; GRAINS ; 89 ; No Details@WHEAT SOYABEAN ; GRAINS ; 89 ; No Details@VINEGAR ; OTHERS ; 90; No Details@BHUNA CHANA,CHILKA CHANA STARCH ; OTHERS ; 90; No Details@BOONDI,SOYA BARI ; OTHERS ; 90; No Details@BAKERY BISCUITS , NAMKEENS ; OTHERS ; 90; No Details@PASTA, MACRON'@MITHASODA, BAKING POWDER VERMICELLI,MURMURE ; OTHERS ;45 ; No Details@CORN FLAKES, AMERICAN CORN POHA MOTA, BAREEK ; OTHERS ;45 ; No Details@MOONS FALI DANA ; OTHERS ;45 ; No Details@ANARDANA,SABUT DANA ARAROT ; OTHERS ;45 ; No Details@URAD BARI, MOONS BARI ; OTHERS ;45 ; No Details@TATA SALT ; SALT ; 72 ; No Details@LITE SALT ; SALT ; 72 ; No Details@BLACK SALT ; SALT ; 72 ; No Details@ROCK SALT (SENDHA) ; SALT ; 72 ; No Details@DOUBLE FILTER PREMIUM SUGAR BURR ; SWEETNERS ; 56; No Details@KARARA ; SWEETNERS ; 56; No Details@GUD ; SWEETNERS ; 56; No Details@SHAKKAR ; SWEETNERS ; 56; No Details"
        //  val data = "m.p sharbati deluxe atta ; atta ; 40 ; no details@mp sharbati atta ; atta ; 38 ; no details@haryana desi atta ; atta ; 30 ; no details@normal fresh atta ; atta ; 24 ; no details@multi grain atta ; atta ; 50 ; no details@jo atta (barley) ; atta ; 80 ; no details@jwar atm chana atta soyabean atta ; atta ; 80 ; no details@makki atta ; atta ; 40 ; no details@bajra atta ; atta ; 30 ; no details@rice powder ; atta ; 40 ; no details@urad dal atta ; atta ; 140 ; no details@moons dal atta ; atta ; 130 ; no details@ragi atta ; atta ; 80 ; no details@besan ; atta ; 90 ; no details@besan (mota) ; atta ; 90 ; no details@maida ; atta ; 40 ; no details@suji ; atta ; 40 ; no details@dalia ; atta ; 40 ; no details@oats ; atta ; 40 ; no details@arhar (took) ; pulses ; 79 ; no details@moons dhuli ; pulses ; 82 ; no details@moons chilka ; pulses ; 80 ; no details@moons sabut ; pulses ; 78 ; no details@urad dhuli ; pulses ; 110 ; no details@urad chilka ; pulses ; 87 ; no details@sabut masur black ; pulses ; 43 ; no details@masur malka ; pulses ; 53 ; no details@rajma chitra, jammu ; pulses ; 52 ; no details@chana dal ; pulses ; 53 ; no details@mix dal ; pulses ; 86 ; no details@moth dal ; pulses ; 24 ; no details@lal lobia, safed lobia ; pulses ; 245 ; no details@matar ; pulses ; 23 ; no details@masur dalli ; pulses ; 26 ; no details@kala chana ; pulses ; 73 ; no details@buli chana ; pulses ; 73 ; no details@sabut garam masala mind moto ; masala ; 78 ; no details@jeera ; masala ; 75 ; no details@long ; masala ; 783 ; no details@kalanoji ; masala ; 34 ; no details@jai fal,javitri ; masala ; 43 ; no details@supari ; masala ; 65 ; no details@tej patta , kadi patta ; masala ; 756 ; no details@kali mirch sabut ; masala ; 768 ; no details@haldi, dhania, amchoor ; masala ; 54 ; no details@lal mirch sabut ; masala ; 44 ; no details@sarso yellow, black ; masala ; 43 ; no details@rai, meth' dana ; masala ; 43 ; no details@saunf mot', bareek ; masala ; 53 ; no details@origanos ; masala ; 36 ; no details@imli,saunth,goond ; masala ; 63 ; no details@extra long grain 1121 xxxl basmati royal rice ; rice ; 57 ; no details@long grain 1121 xxl basmati delight rice ; rice ; 57 ; no details@premium 1121 xl basmati rice ; rice ; 57 ; no details@rozana basmati rice ; rice ; 57 ; no details@basmati tibar(1121) ; rice ; 57 ; no details@basmati dubar (1121) ; rice ; 57 ; no details@sauna masori rice ; rice ; 57 ; no details@parmal double silky rice ; rice ; 57 ; no details@golden sella xxxl 1121 ; rice ; 57 ; no details@golden sella xxl 1121 ; rice ; 57 ; no details@basmati super mogra ; rice ; 57 ; no details@idli rice ; rice ; 57 ; no details@brown rice ; rice ; 57 ; no details@kaju ; dry fruits ; 76 ; no details@badam ; dry fruits ; 76 ; no details@pista,green pista ; dry fruits ; 76 ; no details@akrot girt, sabut ; dry fruits ; 76 ; no details@khumani anjeer ; dry fruits ; 76 ; no details@kharbuj magaj , tarboj magaj ; dry fruits ; 76 ; no details@chuwara ; dry fruits ; 76 ; no details@gola, gola powder ; dry fruits ; 76 ; no details@khaskhas ; dry fruits ; 76 ; no details@bajra jwar; grains ; 89 ; no details@makka .10(barley) ; grains ; 89 ; no details@ragi ; grains ; 89 ; no details@flex seeds(alsi) ; grains ; 89 ; no details@satanaj mix ; grains ; 89 ; no details@wheat soyabean ; grains ; 89 ; no details@vinegar ; others ; 90; no details@bhuna chana,chilka chana starch ; others ; 90; no details@boondi,soya bari ; others ; 90; no details@bakery biscuits , namkeens ; others ; 90; no details@pasta, macron'@mithasoda, baking powder vermicelli,murmure ; others ;45 ; no details@corn flakes, american corn poha mota, bareek ; others ;45 ; no details@moons fali dana ; others ;45 ; no details@anardana,sabut dana ararot ; others ;45 ; no details@urad bari, moons bari ; others ;45 ; no details@tata salt ; salt ; 72 ; no details@lite salt ; salt ; 72 ; no details@black salt ; salt ; 72 ; no details@rock salt (sendha) ; salt ; 72 ; no details@double filter premium sugar burr ; sweetners ; 56; no details@karara ; sweetners ; 56; no details@gud ; sweetners ; 56; no details@shakkar ; sweetners ; 56; no details"
        val data = "m.p sHaRbAtI DeLuXe aTtA ; AtTa ; 40 ; No dEtAiLs@mP ShArBaTi aTtA ; AtTa ; 38 ; No dEtAiLs@hArYaNa dEsI AtTa ; aTtA ; 30 ; nO DeTaIlS@NoRmAl fReSh aTtA ; AtTa ; 24 ; No dEtAiLs@mUlTi gRaIn aTtA ; AtTa ; 50 ; No dEtAiLs@jO AtTa (BaRlEy) ; AtTa ; 80 ; No dEtAiLs@jWaR AtM ChAnA AtTa sOyAbEaN AtTa ; aTtA ; 80 ; nO DeTaIlS@MaKkI AtTa ; aTtA ; 40 ; nO DeTaIlS@BaJrA AtTa ; aTtA ; 30 ; nO DeTaIlS@RiCe pOwDeR ; AtTa ; 40 ; No dEtAiLs@uRaD DaL AtTa ; aTtA ; 140 ; No dEtAiLs@mOoNs dAl aTtA ; AtTa ; 130 ; nO DeTaIlS@RaGi aTtA ; AtTa ; 80 ; No dEtAiLs@bEsAn ; aTtA ; 90 ; nO DeTaIlS@BeSaN (mOtA) ; aTtA ; 90 ; nO DeTaIlS@MaIdA ; AtTa ; 40 ; No dEtAiLs@sUjI ; AtTa ; 40 ; No dEtAiLs@dAlIa ; aTtA ; 40 ; nO DeTaIlS@OaTs ; aTtA ; 40 ; nO DeTaIlS@ArHaR (tOoK) ; pUlSeS ; 79 ; nO DeTaIlS@MoOnS DhUlI ; PuLsEs ; 82 ; No dEtAiLs@mOoNs cHiLkA ; PuLsEs ; 80 ; No dEtAiLs@mOoNs sAbUt ; pUlSeS ; 78 ; nO DeTaIlS@UrAd dHuLi ; pUlSeS ; 110 ; No dEtAiLs@uRaD ChIlKa ; pUlSeS ; 87 ; nO DeTaIlS@SaBuT MaSuR BlAcK ; PuLsEs ; 43 ; No dEtAiLs@mAsUr mAlKa ; pUlSeS ; 53 ; nO DeTaIlS@RaJmA ChItRa, JaMmU ; PuLsEs ; 52 ; No dEtAiLs@cHaNa dAl ; pUlSeS ; 53 ; nO DeTaIlS@MiX DaL ; PuLsEs ; 86 ; No dEtAiLs@mOtH DaL ; PuLsEs ; 24 ; No dEtAiLs@lAl lObIa, SaFeD LoBiA ; PuLsEs ; 245 ; nO DeTaIlS@MaTaR ; PuLsEs ; 23 ; No dEtAiLs@mAsUr dAlLi ; pUlSeS ; 26 ; nO DeTaIlS@KaLa cHaNa ; pUlSeS ; 73 ; nO DeTaIlS@BuLi cHaNa ; pUlSeS ; 73 ; nO DeTaIlS@SaBuT GaRaM MaSaLa mInD MoTo ; mAsAlA ; 78 ; nO DeTaIlS@JeErA ; MaSaLa ; 75 ; No dEtAiLs@lOnG ; MaSaLa ; 783 ; nO DeTaIlS@KaLaNoJi ; mAsAlA ; 34 ; nO DeTaIlS@JaI FaL,JaViTrI ; MaSaLa ; 43 ; No dEtAiLs@sUpArI ; MaSaLa ; 65 ; No dEtAiLs@tEj pAtTa , kAdI PaTtA ; MaSaLa ; 756 ; nO DeTaIlS@KaLi mIrCh sAbUt ; mAsAlA ; 768 ; No dEtAiLs@hAlDi, DhAnIa, AmChOoR ; MaSaLa ; 54 ; No dEtAiLs@lAl mIrCh sAbUt ; mAsAlA ; 44 ; nO DeTaIlS@SaRsO YeLlOw, BlAcK ; MaSaLa ; 43 ; No dEtAiLs@rAi, MeTh' DaNa ; mAsAlA ; 43 ; nO DeTaIlS@SaUnF MoT', BaReEk ; mAsAlA ; 53 ; nO DeTaIlS@OrIgAnOs ; mAsAlA ; 36 ; nO DeTaIlS@ImLi,sAuNtH,GoOnD ; MaSaLa ; 63 ; No dEtAiLs@eXtRa lOnG GrAiN 1121 xXxL BaSmAtI RoYaL RiCe ; rIcE ; 57 ; nO DeTaIlS@LoNg gRaIn 1121 XxL BaSmAtI DeLiGhT RiCe ; rIcE ; 57 ; nO DeTaIlS@PrEmIuM 1121 xL BaSmAtI RiCe ; rIcE ; 57 ; nO DeTaIlS@RoZaNa bAsMaTi rIcE ; RiCe ; 57 ; No dEtAiLs@bAsMaTi tIbAr(1121) ; rIcE ; 57 ; nO DeTaIlS@BaSmAtI DuBaR (1121) ; rIcE ; 57 ; nO DeTaIlS@SaUnA MaSoRi rIcE ; RiCe ; 57 ; No dEtAiLs@pArMaL DoUbLe sIlKy rIcE ; RiCe ; 57 ; No dEtAiLs@gOlDeN SeLlA XxXl 1121 ; RiCe ; 57 ; No dEtAiLs@gOlDeN SeLlA XxL 1121 ; rIcE ; 57 ; nO DeTaIlS@BaSmAtI SuPeR MoGrA ; RiCe ; 57 ; No dEtAiLs@iDlI RiCe ; rIcE ; 57 ; nO DeTaIlS@BrOwN RiCe ; rIcE ; 57 ; nO DeTaIlS@KaJu ; dRy fRuItS ; 76 ; nO DeTaIlS@BaDaM ; DrY FrUiTs ; 76 ; No dEtAiLs@pIsTa,gReEn pIsTa ; dRy fRuItS ; 76 ; nO DeTaIlS@AkRoT GiRt, SaBuT ; DrY FrUiTs ; 76 ; No dEtAiLs@kHuMaNi aNjEeR ; DrY FrUiTs ; 76 ; No dEtAiLs@kHaRbUj mAgAj , tArBoJ MaGaJ ; DrY FrUiTs ; 76 ; No dEtAiLs@cHuWaRa ; dRy fRuItS ; 76 ; nO DeTaIlS@GoLa, GoLa pOwDeR ; DrY FrUiTs ; 76 ; No dEtAiLs@kHaSkHaS ; DrY FrUiTs ; 76 ; No dEtAiLs@bAjRa jWaR; gRaInS ; 89 ; nO DeTaIlS@MaKkA .10(BaRlEy) ; GrAiNs ; 89 ; No dEtAiLs@rAgI ; GrAiNs ; 89 ; No dEtAiLs@fLeX SeEdS(AlSi) ; GrAiNs ; 89 ; No dEtAiLs@sAtAnAj mIx ; gRaInS ; 89 ; nO DeTaIlS@WhEaT SoYaBeAn ; gRaInS ; 89 ; nO DeTaIlS@ViNeGaR ; OtHeRs ; 90; nO DeTaIlS@BhUnA ChAnA,ChIlKa cHaNa sTaRcH ; OtHeRs ; 90; nO DeTaIlS@BoOnDi,sOyA BaRi ; oThErS ; 90; No dEtAiLs@bAkErY BiScUiTs , nAmKeEnS ; OtHeRs ; 90; nO DeTaIlS@PaStA, mAcRoN'@mItHaSoDa, BaKiNg pOwDeR VeRmIcElLi,mUrMuRe ; oThErS ;45 ; No dEtAiLs@cOrN FlAkEs, AmErIcAn cOrN PoHa mOtA, bArEeK ; OtHeRs ;45 ; nO DeTaIlS@MoOnS FaLi dAnA ; OtHeRs ;45 ; nO DeTaIlS@AnArDaNa,sAbUt dAnA ArArOt ; oThErS ;45 ; No dEtAiLs@uRaD BaRi, MoOnS BaRi ; oThErS ;45 ; No dEtAiLs@tAtA SaLt ; sAlT ; 72 ; nO DeTaIlS@LiTe sAlT ; SaLt ; 72 ; No dEtAiLs@bLaCk sAlT ; SaLt ; 72 ; No dEtAiLs@rOcK SaLt (SeNdHa) ; SaLt ; 72 ; No dEtAiLs@dOuBlE FiLtEr pReMiUm sUgAr bUrR ; SwEeTnErS ; 56; No dEtAiLs@kArArA ; SwEeTnErS ; 56; No dEtAiLs@gUd ; sWeEtNeRs ; 56; nO DeTaIlS@ShAkKaR ; SwEeTnErS ; 56; No dEtAiLs"

        val productEntityList = ArrayList<ProductEntity>()

        val productLines = data.split('@')
        for (productLine in productLines)
        {
            show("Data => $productLine")
            val productData = productLine.split(';')

            show("Working=> ${productData[0]}")
            val productEntity = ProductEntity()
            try
            {
                productEntity.name = productData[0]
                productEntity.image = getColorBitmap()
                productEntity.category = productData[1]
                productEntity.price = productData[2]
                productEntity.details = productData[3]
                productEntityList.add(productEntity)

                CoroutineScope(Dispatchers.IO).launch {
                    myDatabase!!.productDao().addNewProduct(productEntity)
                    CoroutineScope(Dispatchers.Main).launch {
                        recyclerviewAdaptor.addProduct(productEntity, 0)
                    }
                }

            } catch (e: IndexOutOfBoundsException)
            {
                productEntity.name = productData[0]
                productEntityList.add(productEntity)
            }

        }


        // recyclerviewAdaptor.addProductList(productEntityList)


    }

    private fun getColorBitmap(): Bitmap
    {
        val random = Random()
        val color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

        val bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(color)

        return bitmap

    }

    private fun show(message: String)
    {
        Log.i("###", message)
    }

    private fun showToast(msg: String)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showSnakeBar(msg: String, color: Int)
    {
        val snackbar = Snackbar.make(reycleviewMain, msg, Snackbar.LENGTH_LONG)
        val sbView: View = snackbar.view
        sbView.setBackgroundColor(this.getColor(color))
        val textView = sbView.findViewById(R.id.snackbar_text) as TextView
        textView.textSize = 15f
        textView.setTextColor(Color.WHITE)
        snackbar.show()
    }

}