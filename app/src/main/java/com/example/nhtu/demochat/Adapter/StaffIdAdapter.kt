package com.example.nhtu.demochat.Adapter

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.Utils.CallToAction
import com.example.nhtu.demochat.Parameter.Parameter
import com.example.nhtu.demochat.R

class StaffIdAdapter : RecyclerView.Adapter<StaffIdAdapter.ViewHolder> {


    private var mContext: Context? = null
    private var mListItem = ArrayList<String>()
    private val actionToParent: CallToAction?

    constructor(context: Context?,action: CallToAction?, listItem: ArrayList<String>) {
        mContext = context
        mListItem = listItem
        actionToParent = action
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtStaffId: TextView?
        var imgRemove: ImageView?

        init {
            txtStaffId = itemView.findViewById(R.id.txtStaffId) as TextView
            imgRemove=itemView.findViewById(R.id.imgRemove) as ImageView

            itemView.setOnClickListener(View.OnClickListener {
                var bundle= Bundle()
                bundle.putInt(Parameter.ActionMain.callAction, Parameter.ActionMain.removeId)
                bundle.putInt(Parameter.BundleParam.position, position)

                actionToParent?.action(bundle)
                mListItem.removeAt(position)
                notifyDataSetChanged()
            })
        }

        fun bind(mListItem: ArrayList<String>, position: Int) {
            txtStaffId?.setText(mListItem[position])
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_staff, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mListItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mListItem?.let { holder.bind(it, position) }
    }
}