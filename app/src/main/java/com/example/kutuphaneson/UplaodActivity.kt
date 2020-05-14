package com.example.kutuphaneson

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_uplaod.*
import kotlinx.android.synthetic.main.recycler_view_row.*
import java.lang.Exception
import java.util.*


class UplaodActivity : AppCompatActivity() {

    var selectedPicture: Uri? = null
    val db = Firebase.firestore
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uplaod)

        auth = FirebaseAuth.getInstance()

    }


    fun uploadimageViewClicked(view: View)
    {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            //request code istediğin rakam olabilir sadece diğerlerinden ayırmak için kullandığımız bir şey
        }
        else
        {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)                                                //Galeriye gittik
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>, grantResults: IntArray) //İzin istediğimde buraya gelirim
    {


        if(requestCode==1)
        {

            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)  //Kullanıcı bir sonuç ile döndüyse (gerçekten cevap verdiyse) ve izin verdiyse
            {

                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,2)

            }


        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)    //Kullanıcı bir fotoğraf seçti şimdi bu activitye gelir
    {

        if(requestCode==2 && resultCode== Activity.RESULT_OK && data!=null)       // Eğer galeriye ulaşmak istemişsem ve kullanıcı fotoğrafı seçip ok demişse ve seçtiği data boş değilse
        {

            selectedPicture = data.data   // Seçilen resmin urı'ını aldım

            try
            {
                if(selectedPicture!=null)  //yukarıda yaptığımız data!=null işlemiyle aynı aslında amaç ekstra güvenlik
                {

                    if(Build.VERSION.SDK_INT>=28)
                    {

                        val source = ImageDecoder.createSource(contentResolver,selectedPicture!!)   //  !! non null olduğunu söyler
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        uploadimageView.setImageBitmap(bitmap)
                    }

                    else
                    {

                        val bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,selectedPicture)
                        uploadimageView.setImageBitmap(bitmap)

                    }


                }
            }

            catch (e:Exception)
            {
                e.printStackTrace()
            }



        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun PaylasClicked(view: View)
    {


        val uuid = UUID.randomUUID()    // Fotoğraflar için rastgele benzersiz bir  id ürettik

        val fotoAdi  = "$uuid+jpg"

        val storage = FirebaseStorage.getInstance()
        val reference = storage.reference
        val imagesReference = reference.child("images").child(fotoAdi) //images klasörüne gider eğer yoksa oluşturur

        if(selectedPicture!=null)
        {
            imagesReference.putFile(selectedPicture!!).addOnSuccessListener {taskSnapshot ->    //Suanda resim stogare'e yüklendi


                val loadedPictureReference = FirebaseStorage.getInstance().reference.child("images").child(fotoAdi)

                 loadedPictureReference.downloadUrl.addOnSuccessListener { uri ->


                     val downloadUri = uri.toString()
                     println(downloadUri)

                     val postMap = hashMapOf<String,Any>()

                     postMap.put("downloadUrl",downloadUri)
                     postMap.put("userEmail",auth.currentUser!!.email.toString())
                     postMap.put("comment",etComment.text.toString())
                     postMap.put("date",Timestamp.now()) // Firebase'ınki kullandık
                     postMap.put("kitapAdi",tvKitapAdi.text.toString())
                     postMap.put("yazarAdi",tvYazarAdi.text.toString())


                     db.collection("Posts").add(postMap).addOnCompleteListener{task ->

                         if(task.isSuccessful)
                         {
                             finish()
                         }

                         else
                         {
                             Toast.makeText(applicationContext,"Bir hata oluştu "+task.exception,Toast.LENGTH_SHORT).show()
                         }

                     }


                 }

             }

            }
        }

        fun ekle()
        {

            println(RecylerBaslikKitap.text.toString())
            println(RecylerBaslikYazar.text.toString())
            println(RecylertvComment.text.toString())




        }




    }


