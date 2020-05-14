package com.example.kutuphaneson

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*



class LoginActivity : AppCompatActivity() {

    //lateinit var myAuthStateListener: FirebaseAuth.AuthStateListener    //her değişikliği kontrol edeceğim yapı (bu bir interface)


    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_login)
        getSupportActionBar()?.hide();      //basliklari gizlemek icin

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

       if(currentUser!=null)
        {
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }                                   // Eğer kullanıcı çıkış yapmadan ekranı kapattıysa uygulamayı açtığında kaldığı yerden devam etsin diye


        uyeolText.setOnClickListener{

            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)

        }

        sifreUnuttuText.setOnClickListener{

            var sifreGonderDialog= SifremiUnuttumFragment()

            sifreGonderDialog.show(supportFragmentManager,"sifreDialogGoster")

        }

        tvOnayMailiTekrar.setOnClickListener{

            var dialogGoster = MailTekrarFragment()

            dialogGoster.show(supportFragmentManager,"dialogGoster")

        }


        BtnGiris.setOnClickListener{

            if(etMail.text.isNotEmpty() && etSifre.text.isNotEmpty())
            {

                progressBarGoster()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(etMail.text.toString(),etSifre.text.toString())   //Giriş yapmayı denedim şimdi bu olayı dinliyorum onComplete ile
                    .addOnCompleteListener(object:OnCompleteListener<AuthResult>{
                        override fun onComplete(p0: Task<AuthResult>) {

                            if(p0.isSuccessful) //giriş yapabildim (burası çalıştığı anda daha bu if bloğu çalışmadan state listener çalışacak)
                            {
                                progressBarGizle()

                                val kullanici = FirebaseAuth.getInstance().currentUser

                                if(kullanici!=null)
                                {

                                    if(kullanici.isEmailVerified)
                                    {
                                        var intent = Intent(this@LoginActivity,MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }

                                    else
                                    {
                                        FirebaseAuth.getInstance().signOut()
                                        Toast.makeText(this@LoginActivity,"Mail Kutunuzu Kontrol Ediniz",Toast.LENGTH_SHORT).show()

                                    }
                                }
                            }

                            else                //giriş yapamadım
                            {
                                progressBarGizle()
                                Toast.makeText(this@LoginActivity,"Giriş Başarısız"+p0.exception?.message,Toast.LENGTH_LONG).show()
                            }
                        }
                    })
            }

            else
            {
                Toast.makeText(this@LoginActivity,"Boş Alanları Doldurunuz",Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun progressBarGoster()
    {
        progressBar2.visibility=View.VISIBLE
    }

    private fun progressBarGizle()
    {
        progressBar2.visibility=View.INVISIBLE
    }


}
