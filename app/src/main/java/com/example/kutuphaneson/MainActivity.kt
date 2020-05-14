package com.example.kutuphaneson


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() ,SearchView.OnQueryTextListener{

   // private lateinit var database: DatabaseReference

    private lateinit var auth : FirebaseAuth
    val db = Firebase.firestore
    var adapter: FeedRecyclerAdapter? = null

    var userEmailFromDb: ArrayList<String> = ArrayList()
    var userCommentFromDb: ArrayList<String> = ArrayList()
    var userImageFromDb: ArrayList<String> = ArrayList()
    var kitapAdiDb: ArrayList<String> = ArrayList()
    var yazarAdiDb: ArrayList<String> = ArrayList()

    var gonderilecekKelime = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        getDataFromFireStore()

        var layoutManager = LinearLayoutManager(this)
        recyleView.layoutManager = layoutManager

        adapter = FeedRecyclerAdapter(userEmailFromDb,userCommentFromDb,userImageFromDb,kitapAdiDb,yazarAdiDb)

        recyleView.adapter = adapter


    }



    private fun getDataFromFireStore()
    {


        //snapshot listener her türlü anlık değişimi izlemeni sağlar
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener{ snapshot, exception->

            if(exception!=null)
            {
                Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()

            }

            else
            {

                if(snapshot!=null && !(snapshot.isEmpty))
                {
                    userImageFromDb.clear()
                    userCommentFromDb.clear()
                    userEmailFromDb.clear()
                    kitapAdiDb.clear()
                    yazarAdiDb.clear()

                    val documents = snapshot.documents

                    for(document in documents)
                    {
                        try{
                            val comment = document.get("comment") as String             //any olarak kaydetmiştik ya dönüşte string olsun istiyoruz
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            val kitapAdi = document.get("kitapAdi").toString().trim()
                            val yazarAdi = document.get("yazarAdi") as String
                            val timeStamp = document.get("date") as com.google.firebase.Timestamp

                            val date = timeStamp.toDate()

                            userEmailFromDb.add(userEmail)
                            userCommentFromDb.add(comment)
                            userImageFromDb.add(downloadUrl)
                            kitapAdiDb.add(kitapAdi)
                            yazarAdiDb.add(yazarAdi)

                            adapter!!.notifyDataSetChanged()
                        }

                        catch (e:Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

    }

    private fun getDataFromFireStoreFilter(filtre:String)
    {

        println(filtre)

        val kucuk_filtre=filtre.toLowerCase()  //hepsini kucuk harf yaptik

        val filtre: String = kucuk_filtre.substring(0, 1).toUpperCase() + kucuk_filtre.substring(1)

        println(filtre)

        db.collection("Posts").orderBy("kitapAdi").startAt(filtre).endAt(filtre+"\uf8ff").addSnapshotListener(){ snapshot, exception->

            if(exception!=null)
            {
                Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()

            }

            else
            {

                if(snapshot!=null && !(snapshot.isEmpty))
                {
                    userImageFromDb.clear()
                    userCommentFromDb.clear()
                    userEmailFromDb.clear()
                    kitapAdiDb.clear()
                    yazarAdiDb.clear()

                    val documents = snapshot.documents

                    for(document in documents)
                    {
                        try{
                            val comment = document.get("comment") as String             //any olarak kaydetmiştik ya dönüşte string olsun istiyoruz
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            val kitapAdi = document.get("kitapAdi").toString().trim()
                            val yazarAdi = document.get("yazarAdi") as String
                            val timeStamp = document.get("date") as com.google.firebase.Timestamp

                            val date = timeStamp.toDate()

                            userEmailFromDb.add(userEmail)
                            userCommentFromDb.add(comment)
                            userImageFromDb.add(downloadUrl)
                            kitapAdiDb.add(kitapAdi)
                            yazarAdiDb.add(yazarAdi)

                            adapter!!.notifyDataSetChanged()
                        }

                        catch (e:Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {        //Burada menüyü oluşturuyoruz

        menuInflater.inflate(R.menu.anamenu,menu)
        menuInflater.inflate(R.menu.searchmenu,menu)

        var aramaItemmenu=menu?.findItem(R.id.app_bar_search)

        var searchView = aramaItemmenu?.actionView as SearchView

        //değişiklik yapıldıkça dinleyecek sınıf
        searchView.setOnQueryTextListener(this) //this dedik cunku degisikleri bu classda dinleyecegiz


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {   //Burada da işlemleri yapıyoruz

        when(item.itemId)
        {


            R.id.menuCikisYap->{

                cikisYap()

                return true

            }

            R.id.menuFotoEkle->{


                fotoEkle()

                return true

            }




        }

        return super.onOptionsItemSelected(item)
    }



    private fun fotoEkle()
    {

        var intent = Intent(this,UplaodActivity::class.java)

        startActivity(intent)


    }

    private fun cikisYap()
    {

        FirebaseAuth.getInstance().signOut()
        var intent = Intent(this,LoginActivity::class.java)

        startActivity(intent)


    }

    override fun onQueryTextSubmit(query: String?): Boolean
    {

        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean
    {


        if(newText.isNullOrEmpty())
        {
            getDataFromFireStore()
        }
        else
        {
            getDataFromFireStoreFilter(newText.toString())
        }


        return true
    }
}
