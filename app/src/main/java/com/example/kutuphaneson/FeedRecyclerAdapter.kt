package com.example.kutuphaneson

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.lang.Exception

//import com.squareup.picasso.Picasso

 //diğer sınıftaki arraylistleri kullanabilmek için constructor oluşturdum (classın içinde de cons() yazıp tanımlayabilirdim)

class FeedRecyclerAdapter(
    private val userEmailArray: ArrayList<String>, private val userCommentArray: ArrayList<String>, private val userImageArray: ArrayList<String>,
    private val kitapBaslikArray: ArrayList<String>, private val kitapYazarArray:ArrayList<String>):
    RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> ()
{



    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): FeedRecyclerAdapter.PostHolder //Viewleri burda bağlayacağız
    {

        var inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_view_row,parent,false)
        return PostHolder(view)

    }

    override fun getItemCount(): Int //kaç tane recycle view row oluşturacağımızı söyleyeceğiz
    {
        return userEmailArray.size

    }

    override fun onBindViewHolder(holder: FeedRecyclerAdapter.PostHolder, position: Int)    //viewlerın içeriğinde neler olacağını yazıcaz
    {

        try{
            holder.emailRecycler?.text = userEmailArray[position]+" bir alıntı ekledi"  //emailrecycler aşağıda postholder içinde tanımladığım, useremail de yukarıda constructorda
            holder.commentRecycler?.text = userCommentArray[position]
            holder.baslikYazarRecycler?.text = kitapYazarArray[position]
            holder.baslikKitapRecycler?.text = kitapBaslikArray[position]


        }

        catch(e:Exception) {
            e.printStackTrace()
        }

        Picasso.get().load(userImageArray[position]).into(holder.fotoRecycler)      //Fotoğrafı picasso kütüphanesi yardımı ile aldık

    }

    class PostHolder(view: android.view.View) : RecyclerView.ViewHolder(view)       // Görünümlerimizi tutan sınıf, viewleri burda tanımlayacağız
    {

        var emailRecycler: TextView? = null
        var fotoRecycler : ImageView? = null
        var commentRecycler :TextView? = null
        var baslikYazarRecycler: TextView?= null
        var baslikKitapRecycler: TextView?= null

        init            // Postholderdan bir obje oluşturulduğunda çağrılacak ilk sınıf
        {

            emailRecycler = view.findViewById(R.id.RecylertvKullaniciEmail)
            fotoRecycler = view.findViewById(R.id.RecylerimageView)
            commentRecycler = view.findViewById(R.id.RecylertvComment)
            baslikKitapRecycler = view.findViewById(R.id.RecylerBaslikKitap)
            baslikYazarRecycler = view.findViewById(R.id.RecylerBaslikYazar)


        }


    }




}



