package com.example.nhtu.demochat.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.nhtu.demochat.Adapter.StaffIdAdapter
import com.example.nhtu.demochat.Api.ApiService
import com.example.nhtu.demochat.Base.BaseActivity
import com.example.nhtu.demochat.Model.GetSessionInput
import com.example.nhtu.demochat.Model.GetSessionOutput
import com.example.nhtu.demochat.Model.SocketExpire
import com.example.nhtu.demochat.R
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity(), View.OnClickListener {


    private var session:String?=null
    private var socket: Socket?=null
    var contractName="SGH123456"
    var apiKey="fbdd810b6c6951fc5662e87bac347e4f62d06263"
    var from="2019-06-07T00:00:00.000Z"
    var to="2019-05-08T12:59:59.000Z"
    var employeeID1="AA"
    var employeeID2="BB"
    var listStaffId=ArrayList<String>()

    private var staffIdAdapter:StaffIdAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initValue()
        initListener()
    }

    private fun initValue() {
        staffIdAdapter= StaffIdAdapter(this,listStaffId)
        rcvListStaff?.adapter=staffIdAdapter
        rcvListStaff?.layoutManager=LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
    }

    private fun initListener() {
        btnAdd?.setOnClickListener(this)
        btnStart?.setOnClickListener(this)
    }


    private fun getSesion() {
        var input= GetSessionInput()
        input.contractName=contractName
        input.apiKey=apiKey
        input.socketExpire= SocketExpire(from, to)
        var tracking=ArrayList<String>()
        input.tracking=listStaffId

        ApiService.getInstance().api?.getSession(input)?.enqueue(object :retrofit2.Callback<GetSessionOutput>{
            override fun onFailure(call: Call<GetSessionOutput>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<GetSessionOutput>, response: Response<GetSessionOutput>) {
                if (call != null && !call.isCanceled && response.isSuccessful){
                    var output: GetSessionOutput?=null
                    if (response!=null){
                        output=response.body()
                    }
                    if (output?.ErrorCode==0){
                        session=(response.body() as GetSessionOutput).result?.sessionID
                        //showMessage(session.toString())
                        var bundle=Bundle()
                        bundle.putString("session",session)
                        var intent= Intent(this@MainActivity, TrackingActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@MainActivity,output?.Message,Toast.LENGTH_LONG).show()
                    }
                }


            }
        })

    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAdd->{
                var id=edtInputStaff.text.toString() as String
                if (listStaffId?.contains(id)){
                    Toast.makeText(this,"Mã nhân viên đã có",Toast.LENGTH_LONG).show()
                }
                else{
                    listStaffId.add(id)
                    staffIdAdapter?.notifyDataSetChanged()
                }
            }
            R.id.btnStart->{
                contractName=edtContractName?.text.toString()
                apiKey=edtApiKey?.text.toString()
                from=getCurrentDateTime()
                to=getExpiredDay()
                getSesion()
            }
        }
    }

    private fun getExpiredDay(): String {
        var calendar=Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR,1)
        val expireDay=calendar.time
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'12:59:59'.000Z'")
        return formatter.format(expireDay)
    }

    private fun getCurrentDateTime(): String {
        val current = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00'.000Z'")
        return formatter.format(current)
    }
}
