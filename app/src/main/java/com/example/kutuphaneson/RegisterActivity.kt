package com.example.kutuphaneson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getSupportActionBar()?.hide();

        BtnKayitOl.setOnClickListener{

            if (emailEditSifreUnuttum.text.isNotEmpty() && passwordEdit.text.isNotEmpty() && passwordAgainEdit.text.isNotEmpty())
            {
                if (passwordEdit.text.toString().equals(passwordAgainEdit.text.toString()))
                {
                    progressBarGoster()
                    yeniUyeKayit(emailEditSifreUnuttum.text.toString(), passwordEdit.text.toString())
                }
                else
                {
                    Toast.makeText(this, "Şifreler aynı değil", Toast.LENGTH_SHORT).show()
                }
            }

            else
            {
                Toast.makeText(this, "Boş alanları doldurunuz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun yeniUyeKayit(mail: String, sifre: String)
    {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, sifre)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {
                    if (p0.isSuccessful)
                    {
                        progressBarGizle()
                        Toast.makeText(this@RegisterActivity, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
                        onayMailiGonder()

                    }
                    else
                    {
                        progressBarGizle()
                        Toast.makeText(this@RegisterActivity, "Kayıt sırasında bir sorun oluştu:" + p0.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })


    }

    private fun onayMailiGonder()
    {

        var kullanici= FirebaseAuth.getInstance().currentUser

        if(kullanici != null)
        {

            kullanici.sendEmailVerification()
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        if(p0.isSuccessful)
                        {
                            Toast.makeText(this@RegisterActivity, "Mail kutunuzu kontrol edin, mailiniz onaylayın", Toast.LENGTH_LONG).show()
                        }

                        else
                        {
                            Toast.makeText(this@RegisterActivity, "Mail göndeirlirken sorun oluştu " + p0.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }

                })

        }

    }


    private fun progressBarGoster()
    {
        progressBar.visibility = View.VISIBLE
    }

    private fun progressBarGizle()
    {
        progressBar.visibility = View.INVISIBLE
    }
}
