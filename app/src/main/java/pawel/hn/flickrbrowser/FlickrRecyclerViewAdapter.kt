package pawel.hn.flickrbrowser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_flickr.view.*


class FlickrRecyclerViewAdapter(
    private val context: Context,
    private var list: List<PhotoArrayGSON>
) : RecyclerView.Adapter<MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewHolder = LayoutInflater.from(context).inflate(R.layout.item_flickr, parent, false)
        return MyViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (list.isEmpty()) {
            holder.itemView.tvFlickrTitle.text = "no photo found - might be connection issue"
            holder.itemView.ivFlickrImage.setImageResource(R.drawable.ic_placeholder)
        } else {
            val model = list[position]
            holder.itemView.tvFlickrAuthor.text = model.author
            holder.itemView.tvFlickrTitle.text = model.title

            Picasso.get()
                .load(model.media.replaceMwithB())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .fit()
                .into(holder.itemView.ivFlickrImage)

            holder.itemView.ivFlickrImage.setOnClickListener {
                val intent = Intent(context, FlickrDetailActivity::class.java)
                intent.putExtra(FLICKR_TRANSFER, model)
                startActivity(context, intent, null)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) 1 else list.size
    }

    fun getFlickrImagePosition(position: Int): PhotoArrayGSON? {
        return if (list.isNotEmpty()) list[position] else null
    }

    fun loadNewData(newData: List<PhotoArrayGSON>) {
        list = newData
        notifyDataSetChanged()

    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)