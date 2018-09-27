package app.volMP.u.rel

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_acc_information.*
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

class AccInformation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acc_information)

        val phoneSlots = UnderscoreDigitSlotsParser().parseSlots("+_(___)-___-__-__")

        MaskFormatWatcher(MaskImpl.createTerminated(phoneSlots)).installOn(tPhone)

        val sharedPref = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val utoken = sharedPref.getString("utoken","")
        fuelGetUinfo(utoken)
        tName.afterTextChanged { tiName.error = null }
        tSecName.afterTextChanged { tiSecName.error = null }
        tMidName.afterTextChanged { tiMidName.error = null }
        tPhone.afterTextChanged { tiPhone.error = null }
        tSiz.afterTextChanged { tiSiz.error = null }
        tGroup.afterTextChanged { tiGroup.error = null }

        buUpdateIn.setOnClickListener {
            onBuRegInClick(utoken)
        }
    }
    private fun onBuRegInClick(utoken:String){

        when{
        //Фамилия
            tSecName.text.isEmpty() -> {
                tiSecName.error = "Введите фамилию"
            }
            tSecName.text.length > 20 -> {
                tiSecName.error = "Слишком длинная фамилия"
            }
        //Имя
            tName.text.isEmpty() -> {
                tiName.error = "Введите имя"
            }
            tName.text.length > 20 -> {
                tiSecName.error = "Слишком длинное имя"
            }
        //Отчество
            tMidName.text.isEmpty() -> {
                tiMidName.error = "Введите отчество"
            }
            tMidName.text.length > 20 -> {
                tiSecName.error = "Слишком длинное отчество"
            }
        //Телефон
            tPhone.text.isEmpty() -> {
                tiPhone.error = "Введите телефон"
            }
            tPhone.text.length < 17 -> {
                tiPhone.error = "Введите верный телефон"
            }
        //Размер одежды
            tSiz.text.isEmpty() -> {
                tiSiz.error = "Введите размер одежды"
            }
            tSiz.text.length > 6 -> {
                tiSiz.error = "Пример размера: L, XL и тд"
            }
            tGroup.text.isEmpty()->{
                tiGroup.error = "Введите уч. группу"
            }
            tGroup.text.length > 30 ->{
                tiGroup.error = "Введите уч. группу короче 30 символов"
            }

            else ->{
                val user = User(0, utoken, 0, tEmail.text.toString(), 0, tName.text.toString(),
                        tSecName.text.toString(), false, tMidName.text.toString(), tPhone.text.toString(),
                        tSiz.text.toString(), tInfo.text.toString(), tGroup.text.toString())
                updatetext(user)

            }
        }
    }
    private fun fuelGetUinfo(utoken:String) {
        Fuel.post(routes.SERVER + routes.GET_UINFO,
                listOf("utoken" to utoken))
                .response { _, _, result ->

            when (result) {
                is Result.Failure -> {
                    val ex = result.error.exception.message
                    println(ex)
                }
                is Result.Success -> {
                    val data = result.get()
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val user: User = gson.fromJson(String(data, Charsets.UTF_8), User::class.java)
                    runOnUiThread { setTextUser(user) }

                }
            }
        }
    }
    private fun setTextUser(user: User){
        tEmail.setText(user.uemail)

        tName.setText(user.uiname)
        tSecName.setText(user.uisecname)
        tMidName.setText(user.uimidname)
        tPhone.setText(user.uiphone)
        tSiz.setText(user.uisize)
        tInfo.setText(user.uiinfo)
        tGroup.setText(user.uigroup)
    }

    private fun updatetext(user: User){
        val sharedPref = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val utoken = sharedPref.getString("utoken","")
        val url = routes.SERVER + routes.UPDATEUINFO

        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        val stringRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { res ->
                    // success code
                    when {
                        res.toString() == "[3]" ->{
                            Toast.makeText(this, "Ошибка, попробуйте еще раз",
                                    Toast.LENGTH_LONG).show()
                            toMainScreenForm()

                        }
                        else->{
                            Toast.makeText(this, "OK",
                                    Toast.LENGTH_LONG).show()
                            toMainScreenForm()
                        }
                    }
                },
                Response.ErrorListener { er ->
                    // error code
                    val myToast = Toast.makeText(this, "ERROR: %s".format(er.toString()),
                            Toast.LENGTH_SHORT)
                    myToast.show()
                }) {
            override fun getParams(): Map<String, String> = mapOf("uSecName" to user.uisecname,
                    "uName" to user.uiname, "uMidName" to user.uimidname, "uPhone" to user.uiphone,
                    "uSize" to user.uisize, "uInfo" to user.uiinfo, "uToken" to utoken,
                    "uiGroup" to user.uigroup)
        }
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)
    }
    fun toMainScreenForm(){
        // Create an Intent to start the UserInformation activity
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        // Start the new activity.
        startActivity(mainActivityIntent)
    }
    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }
}
