package app.volMP.u.rel

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.user_rating_row.view.*


/**
 * Created by brianvoong on 12/18/17.
 */

class RatingAdapter(val rating: List<Rating>): RecyclerView.Adapter<RatingAdapter.CustomViewHolder>() {

    // numberOfItems
    override fun getItemCount(): Int {
        return rating.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_rating_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//        val videoTitle = videoTitles.get(position)
        val rat = rating[position]
        holder.view.tNumber?.text = (position+1).toString()
        holder.view.tName?.text = rat.uiname
        holder.view.tSecName?.text = rat.uisecname
        holder.view.tPoints?.text = rat.sum.toString()

        holder.view.tWordPoints?.text = getNumEnding(rat.sum, arrayOf("балл","балла","баллов"))

        holder.rating = rat
    }
    private fun getNumEnding(iNumber: Int, aEndings: Array<String>):String{
        val numbers = iNumber % 100
        return if (numbers in 11..19) {
            aEndings[2]
        } else {
            val i = numbers % 10
            when (i) {
                1 -> {
                    aEndings[0]
                }
                in 2..4 -> {
                    aEndings[1]
                }
                else -> aEndings[2]
            }
        }

    }

    class CustomViewHolder(val view: View, var rating: Rating? = null): RecyclerView.ViewHolder(view)



}