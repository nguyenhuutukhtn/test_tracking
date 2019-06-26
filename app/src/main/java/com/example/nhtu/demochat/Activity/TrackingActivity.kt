package com.example.nhtu.demochat.Activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.Utils.CallToAction
import com.example.nhtu.demochat.Adapter.StaffIdAdapter
import com.example.nhtu.demochat.Base.BaseActivity
import com.example.nhtu.demochat.Model.TrackingOutPut
import com.example.nhtu.demochat.Parameter.Parameter
import com.example.nhtu.demochat.R
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.engineio.client.transports.Polling
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

import kotlinx.android.synthetic.main.activity_tracking.*
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import org.json.JSONObject
import java.net.URI
import java.net.URISyntaxException

class TrackingActivity : BaseActivity(), View.OnClickListener {


    var session=""
    var socket: Socket?=null
    var namespaceSocket:Socket?=null

    var stringListId=""
    var listId=ArrayList<String>()

    var listIdAdapter:StaffIdAdapter?=null
    private var actionFromChild: CallToAction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)
        initvalues()
        initListener()
        getSession()
        createSocket()
    }

    private fun initListener() {
        btnAdd?.setOnClickListener(this)
        btnSend?.setOnClickListener(this)
    }

    private fun initvalues() {
        listIdAdapter= StaffIdAdapter(this,actionFromChild,listId)
        rcvListId?.adapter=listIdAdapter
        rcvListId?.layoutManager= LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
        initListId()
        actionFromChild = object : CallToAction {
            override fun action(bundle: Bundle) {
                if (bundle == null) return
                var positon=bundle.getInt(Parameter.BundleParam.position)
                updateListId(positon)
            }
        }
    }

    private fun initListId() {
        listId.add("00095")
        listId.add("00108231")
        listId.add("000138805")
        listId.add("00076504")
        listId.add("00008461")
    }
    private fun updateListId(positon: Int) {
        listId.removeAt(positon)
        listIdAdapter= StaffIdAdapter(this,actionFromChild,listId)
        listIdAdapter?.notifyDataSetChanged()
    }
    private fun getSession() {
        var bundle=intent.extras
        if (bundle!=null){
            session=intent.extras.getString("session","")
        }
    }

    private fun createSocket() {
        if (session!=null&&session!="")
        {
            val opts = IO.Options()
            opts.forceNew=true
            opts.timeout=20000;
            opts.secure=true
            opts.query = "sessionID="+session+"&partner=true"
            opts.path="/socket/socket.io"
            opts.transports=arrayOf(Polling.NAME,com.github.nkzawa.engineio.client.transports.WebSocket.NAME)
            opts.reconnection = true

            //var manager=Manager(URI(Parameter.apiURL))
            try {
                socket= IO.socket(Parameter.apiURL,opts)

                namespaceSocket= Socket(socket?.io(),"/partner")
            } catch (e:URISyntaxException){
                e.printStackTrace()
            }

            addListenerForSocketEmitter()
            namespaceSocket?.connect()
                ?.on(Socket.EVENT_CONNECT) {
                    showMessage("Connect success")
                    //namespaceSocket?.emit("tracking_partner",stringListId)
                }
                ?.on(Socket.EVENT_DISCONNECT) {
                    showMessage("Disconnected")
                }
                ?.on(Socket.EVENT_MESSAGE) {

                }
                ?.on(Socket.EVENT_ERROR,object : Emitter.Listener{
                    override fun call(vararg args: Any?) {
                        var jsonObject:JsonObject?=null
                        try{
                            jsonObject=args[0] as JsonObject
                        } catch (e:JSONException){
                            e.printStackTrace()
                        }
                        showMessage("Error:"+jsonObject.toString())
                    }
                })
                ?.on(Socket.EVENT_RECONNECTING) {
                    showMessage("Connecting")
                }


        }
    }


    private fun addListenerForSocketEmitter() {


        namespaceSocket?.on("location user realtime") { args ->
            var jsonObject:JSONObject?=null
            try{
                jsonObject=args[0] as JSONObject
            } catch (e:JSONException){
                e.printStackTrace()
            }
            var parser = JsonParser()
            var mJson=parser.parse(jsonObject.toString()) as JsonElement
            var gson=Gson()
            var output=gson.fromJson(mJson,TrackingOutPut::class.java) as TrackingOutPut
            runOnUiThread {
                txtLocation.text=output.location
                txtDevice.text=output.DeviceIMEI
                txtUserTracking.text=output.UserTracking
                txtId.text=output.CodeEmployee
            }
        }

        namespaceSocket?.on("expire",Emitter.Listener {
                fun call(vararg args: Any?) {
                    var jsonObject:JsonObject?=null
                    try{
                        jsonObject=args[0] as JsonObject
                    } catch (e:JSONException){
                        e.printStackTrace()
                    }
                    var parser = JsonParser()
                    var mJson=parser.parse(jsonObject.toString()) as JsonElement
                    showMessage(jsonObject.toString())
                }
            })
        namespaceSocket?.on("expired",Emitter.Listener {
                fun call(vararg args: Any?) {
                    var jsonObject:JsonObject?=null
                    try{
                        jsonObject=args[0] as JsonObject
                    } catch (e:JSONException){
                        e.printStackTrace()
                    }
                    var parser = JsonParser()
                    var mJson=parser.parse(jsonObject.toString()) as JsonElement
                    showMessage(jsonObject.toString())
                }
            })
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAdd->{
                var id=edtInput.text.toString() as String
                if (listId?.contains(id)){
                    Toast.makeText(this,"Mã nhân viên đã có",Toast.LENGTH_LONG).show()
                }
                else{
                    listId.add(id)
                    listIdAdapter?.notifyDataSetChanged()
                }
            }
            R.id.btnSend->{
                if(listId.size==1){
                    stringListId="[\""+listId[0]+"\"]"
                }
                else if (listId.size==0){
                    stringListId="[]"
                }
                else{
                    for (i in 0 until listId.size){
                        if (i==0)
                            stringListId="[\""+listId[0]+"\","
                        else if (i==listId.size-1)
                            stringListId=stringListId+",\""+listId[i]+"\"]"
                        else
                            stringListId=stringListId+",\""+listId[i]+"\""
                    }
                }
                namespaceSocket?.emit("tracking_partner",stringListId)
            }
        }
    }
}
