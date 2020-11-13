package com.ash.shopadminlogin.database


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

@Database(entities = [(ProductEntity::class),(CustomerEntity::class)] ,version = 1 )
abstract class MyDatabase : RoomDatabase()
{
    abstract fun productDao(): DAOProductEntity
    abstract fun acceptedOrderDao(): DAOCustomerEntity


    companion object
    {
        private var INSTANCE: MyDatabase? = null
        fun getInstance(context: Context): MyDatabase
        {
            if (INSTANCE == null)
            {
                INSTANCE = Room.databaseBuilder(
                    context, MyDatabase::class.java, "NoteEntity"
                ).fallbackToDestructiveMigration() //Delete database on changing version
                    .addCallback(roomCallback) //Adding callback
                    .build()
            }

            return INSTANCE as MyDatabase
        }

        val roomCallback = object : RoomDatabase.Callback()
        {
            override fun onCreate(db: SupportSQLiteDatabase)
            {
                super.onCreate(db)
//                CoroutineScope(Dispatchers.IO).launch {
//                    populateDB(INSTANCE!!)
//                }
            }
        }

        private fun populateDB(db: MyDatabase)
        {
            val productDao = db.productDao()
            val data = "m.p sHaRbAtI DeLuXe aTtA ; AtTa ; 40 ; No dEtAiLs@mP ShArBaTi aTtA ; AtTa ; 38 ; No dEtAiLs@hArYaNa dEsI AtTa ; aTtA ; 30 ; nO DeTaIlS@NoRmAl fReSh aTtA ; AtTa ; 24 ; No dEtAiLs@mUlTi gRaIn aTtA ; AtTa ; 50 ; No dEtAiLs@jO AtTa (BaRlEy) ; AtTa ; 80 ; No dEtAiLs@jWaR AtM ChAnA AtTa sOyAbEaN AtTa ; aTtA ; 80 ; nO DeTaIlS@MaKkI AtTa ; aTtA ; 40 ; nO DeTaIlS@BaJrA AtTa ; aTtA ; 30 ; nO DeTaIlS@RiCe pOwDeR ; AtTa ; 40 ; No dEtAiLs@uRaD DaL AtTa ; aTtA ; 140 ; No dEtAiLs@mOoNs dAl aTtA ; AtTa ; 130 ; nO DeTaIlS@RaGi aTtA ; AtTa ; 80 ; No dEtAiLs@bEsAn ; aTtA ; 90 ; nO DeTaIlS@BeSaN (mOtA) ; aTtA ; 90 ; nO DeTaIlS@MaIdA ; AtTa ; 40 ; No dEtAiLs@sUjI ; AtTa ; 40 ; No dEtAiLs@dAlIa ; aTtA ; 40 ; nO DeTaIlS@OaTs ; aTtA ; 40 ; nO DeTaIlS@ArHaR (tOoK) ; pUlSeS ; 79 ; nO DeTaIlS@MoOnS DhUlI ; PuLsEs ; 82 ; No dEtAiLs@mOoNs cHiLkA ; PuLsEs ; 80 ; No dEtAiLs@mOoNs sAbUt ; pUlSeS ; 78 ; nO DeTaIlS@UrAd dHuLi ; pUlSeS ; 110 ; No dEtAiLs@uRaD ChIlKa ; pUlSeS ; 87 ; nO DeTaIlS@SaBuT MaSuR BlAcK ; PuLsEs ; 43 ; No dEtAiLs@mAsUr mAlKa ; pUlSeS ; 53 ; nO DeTaIlS@RaJmA ChItRa, JaMmU ; PuLsEs ; 52 ; No dEtAiLs@cHaNa dAl ; pUlSeS ; 53 ; nO DeTaIlS@MiX DaL ; PuLsEs ; 86 ; No dEtAiLs@mOtH DaL ; PuLsEs ; 24 ; No dEtAiLs@lAl lObIa, SaFeD LoBiA ; PuLsEs ; 245 ; nO DeTaIlS@MaTaR ; PuLsEs ; 23 ; No dEtAiLs@mAsUr dAlLi ; pUlSeS ; 26 ; nO DeTaIlS@KaLa cHaNa ; pUlSeS ; 73 ; nO DeTaIlS@BuLi cHaNa ; pUlSeS ; 73 ; nO DeTaIlS@SaBuT GaRaM MaSaLa mInD MoTo ; mAsAlA ; 78 ; nO DeTaIlS@JeErA ; MaSaLa ; 75 ; No dEtAiLs@lOnG ; MaSaLa ; 783 ; nO DeTaIlS@KaLaNoJi ; mAsAlA ; 34 ; nO DeTaIlS@JaI FaL,JaViTrI ; MaSaLa ; 43 ; No dEtAiLs@sUpArI ; MaSaLa ; 65 ; No dEtAiLs@tEj pAtTa , kAdI PaTtA ; MaSaLa ; 756 ; nO DeTaIlS@KaLi mIrCh sAbUt ; mAsAlA ; 768 ; No dEtAiLs@hAlDi, DhAnIa, AmChOoR ; MaSaLa ; 54 ; No dEtAiLs@lAl mIrCh sAbUt ; mAsAlA ; 44 ; nO DeTaIlS@SaRsO YeLlOw, BlAcK ; MaSaLa ; 43 ; No dEtAiLs@rAi, MeTh' DaNa ; mAsAlA ; 43 ; nO DeTaIlS@SaUnF MoT', BaReEk ; mAsAlA ; 53 ; nO DeTaIlS@OrIgAnOs ; mAsAlA ; 36 ; nO DeTaIlS@ImLi,sAuNtH,GoOnD ; MaSaLa ; 63 ; No dEtAiLs@eXtRa lOnG GrAiN 1121 xXxL BaSmAtI RoYaL RiCe ; rIcE ; 57 ; nO DeTaIlS@LoNg gRaIn 1121 XxL BaSmAtI DeLiGhT RiCe ; rIcE ; 57 ; nO DeTaIlS@PrEmIuM 1121 xL BaSmAtI RiCe ; rIcE ; 57 ; nO DeTaIlS@RoZaNa bAsMaTi rIcE ; RiCe ; 57 ; No dEtAiLs@bAsMaTi tIbAr(1121) ; rIcE ; 57 ; nO DeTaIlS@BaSmAtI DuBaR (1121) ; rIcE ; 57 ; nO DeTaIlS@SaUnA MaSoRi rIcE ; RiCe ; 57 ; No dEtAiLs@pArMaL DoUbLe sIlKy rIcE ; RiCe ; 57 ; No dEtAiLs@gOlDeN SeLlA XxXl 1121 ; RiCe ; 57 ; No dEtAiLs@gOlDeN SeLlA XxL 1121 ; rIcE ; 57 ; nO DeTaIlS@BaSmAtI SuPeR MoGrA ; RiCe ; 57 ; No dEtAiLs@iDlI RiCe ; rIcE ; 57 ; nO DeTaIlS@BrOwN RiCe ; rIcE ; 57 ; nO DeTaIlS@KaJu ; dRy fRuItS ; 76 ; nO DeTaIlS@BaDaM ; DrY FrUiTs ; 76 ; No dEtAiLs@pIsTa,gReEn pIsTa ; dRy fRuItS ; 76 ; nO DeTaIlS@AkRoT GiRt, SaBuT ; DrY FrUiTs ; 76 ; No dEtAiLs@kHuMaNi aNjEeR ; DrY FrUiTs ; 76 ; No dEtAiLs@kHaRbUj mAgAj , tArBoJ MaGaJ ; DrY FrUiTs ; 76 ; No dEtAiLs@cHuWaRa ; dRy fRuItS ; 76 ; nO DeTaIlS@GoLa, GoLa pOwDeR ; DrY FrUiTs ; 76 ; No dEtAiLs@kHaSkHaS ; DrY FrUiTs ; 76 ; No dEtAiLs@bAjRa jWaR; gRaInS ; 89 ; nO DeTaIlS@MaKkA .10(BaRlEy) ; GrAiNs ; 89 ; No dEtAiLs@rAgI ; GrAiNs ; 89 ; No dEtAiLs@fLeX SeEdS(AlSi) ; GrAiNs ; 89 ; No dEtAiLs@sAtAnAj mIx ; gRaInS ; 89 ; nO DeTaIlS@WhEaT SoYaBeAn ; gRaInS ; 89 ; nO DeTaIlS@ViNeGaR ; OtHeRs ; 90; nO DeTaIlS@BhUnA ChAnA,ChIlKa cHaNa sTaRcH ; OtHeRs ; 90; nO DeTaIlS@BoOnDi,sOyA BaRi ; oThErS ; 90; No dEtAiLs@bAkErY BiScUiTs , nAmKeEnS ; OtHeRs ; 90; nO DeTaIlS@PaStA, mAcRoN'@mItHaSoDa, BaKiNg pOwDeR VeRmIcElLi,mUrMuRe ; oThErS ;45 ; No dEtAiLs@cOrN FlAkEs, AmErIcAn cOrN PoHa mOtA, bArEeK ; OtHeRs ;45 ; nO DeTaIlS@MoOnS FaLi dAnA ; OtHeRs ;45 ; nO DeTaIlS@AnArDaNa,sAbUt dAnA ArArOt ; oThErS ;45 ; No dEtAiLs@uRaD BaRi, MoOnS BaRi ; oThErS ;45 ; No dEtAiLs@tAtA SaLt ; sAlT ; 72 ; nO DeTaIlS@LiTe sAlT ; SaLt ; 72 ; No dEtAiLs@bLaCk sAlT ; SaLt ; 72 ; No dEtAiLs@rOcK SaLt (SeNdHa) ; SaLt ; 72 ; No dEtAiLs@dOuBlE FiLtEr pReMiUm sUgAr bUrR ; SwEeTnErS ; 56; No dEtAiLs@kArArA ; SwEeTnErS ; 56; No dEtAiLs@gUd ; sWeEtNeRs ; 56; nO DeTaIlS@ShAkKaR ; SwEeTnErS ; 56; No dEtAiLs"

            val productLines = data.split('@')
            val productEntityList = ArrayList<ProductEntity>()
            for (productLine in productLines)
            {
                show("Data => $productLine")
                val productData = productLine.split(';')

                show("Working=> ${productData[0]}")
                val productEntity = ProductEntity()
                try
                {
                    productEntity.name = productData[0]
                   // productEntity.image = getColorBitmap()
                    productEntity.category = productData[1]
                    productEntity.price = productData[2]
                    productEntity.details = productData[3]
                   productEntityList.add(productEntity)

                } catch (e: Exception)
                {
                    show("Error -> MyDatabase Populate DB failed")
                }

            }
            productDao.insertAll(productEntityList)
        }


        private fun show(message: String)
        {
            Log.i("###", message)
        }




    }




}