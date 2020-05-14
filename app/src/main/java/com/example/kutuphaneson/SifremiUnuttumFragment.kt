package com.example.kutuphaneson

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sifremi_unuttum.*


class SifremiUnuttumFragment : DialogFragment() {


     lateinit var mContext: FragmentActivity
    lateinit var emailText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        var view=inflater.inflate(R.layout.fragment_sifremi_unuttum, container, false)

        emailText= view.findViewById(R.id.emailEditSifreUnuttum)

        var btnIptal = view.findViewById<Button>(R.id.btnIptalSifreUnuttum)





        btnIptal.setOnClickListener  {

            dialog?.dismiss()

        }

        var btnGonder=view.findViewById<Button>(R.id.btnGonderSifreUnuttum1)

           btnGonder.setOnClickListener {

               val mgr: InputMethodManager =
                   activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
               mgr.hideSoftInputFromWindow(btnGonder.getWindowToken(), 0)                          //klavyeyi diseppear etmek icin


             if(emailText.text.isEmpty())
             {
                 Toast.makeText(context,"Lütfen mail adresinizi giriniz", Toast.LENGTH_LONG).show()
             }

            progressBarSifreUnuttum.visibility=View.VISIBLE

            FirebaseAuth.getInstance().sendPasswordResetEmail(emailText?.text.toString()).addOnCompleteListener {task ->

                if(task.isSuccessful)
                {

                    Toast.makeText(activity ,"Mailinizi Kontrol Edin", Toast.LENGTH_LONG).show()
                   // Toast.makeText(activity,"Şifre sıfırlama mailiniz gönderilmiştir",Toast.LENGTH_SHORT)
                }

                else
                {
                    Toast.makeText(activity,"Email adresi hatalı veya kayıtlı değil",Toast.LENGTH_SHORT).show()
                }

            }

            progressBarSifreUnuttum.visibility=View.INVISIBLE
            //dialog?.dismiss()

        }


        return view
    }

}
