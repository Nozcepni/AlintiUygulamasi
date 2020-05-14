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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_mail_tekrar.*


class MailTekrarFragment : DialogFragment() {

    lateinit var emailEt: EditText
    lateinit var sifreEt: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,    //inflater xml'in javaya dönüştürülmesi
        savedInstanceState: Bundle?
    ): View? {

        var view=inflater.inflate(R.layout.fragment_mail_tekrar, container, false)  //Fragmentlerde nesnelere bu view üzerinden ulaşabilirsin ancak. Activityde olduğu gibi
                                                                                                //direk ulaşamassın

        emailEt  = view.findViewById(R.id.etDialogMail)
        sifreEt  = view.findViewById(R.id.etDialogSifre)


        var btnIptal=view.findViewById<Button>(R.id.btnFragmentiptal)

        btnIptal.setOnClickListener {
            dialog?.dismiss()
        }

        var btnGonder=view.findViewById<Button>(R.id.btnFragmentGonder)

        btnGonder.setOnClickListener {

            val mgr: InputMethodManager =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mgr.hideSoftInputFromWindow(btnFragmentGonder.getWindowToken(), 0)

            if(emailEt.text.isNotEmpty() && sifreEt.text.isNotEmpty())
            {

                onayMailiniGonder(emailEt.text.toString(),sifreEt.text.toString())

            }

            else
            {

                Toast.makeText(activity,"Boş Alanları Doldurunuz",Toast.LENGTH_SHORT).show()    //activity diyerek bu diyaloğu çağıran activityi çağırıyorum
                                                                                                     //this diyemem

            }


        }


        return view
    }

    private fun onayMailiniGonder(email: String, sifre: String)
    {

        progressBar3.visibility=View.VISIBLE

        var credential = EmailAuthProvider.getCredential(email,sifre)
        FirebaseAuth.getInstance().signInWithCredential(credential)                                 //sign in email and password den bir farkı yok sadece ikinci bir kullanım şekli
            .addOnCompleteListener{ task ->                                                         //diğer türlü olsa p0 a denk geliyordu

                if(task.isSuccessful)
                {


                    var kullanici= FirebaseAuth.getInstance().currentUser

                    if(kullanici != null)
                    {

                        kullanici.sendEmailVerification()
                            .addOnCompleteListener(object : OnCompleteListener<Void> {
                                override fun onComplete(p0: Task<Void>) {
                                    if(p0.isSuccessful)
                                    {

                                        dialog?.dismiss()
                                        Toast.makeText(activity, "Mail kutunuzu kontrol ediniz ", Toast.LENGTH_LONG).show()
                                    }

                                    else
                                    {
                                        Toast.makeText(activity, "Mail gönderilirken sorun oluştu " + p0.exception?.message, Toast.LENGTH_LONG).show()
                                    }
                                }

                            })


                    }


                }

                else
                {
                    Toast.makeText(activity,"Email veya Sifre Hatalı",Toast.LENGTH_SHORT).show()
                }


            }

        progressBar3.visibility=View.INVISIBLE

    }


}
