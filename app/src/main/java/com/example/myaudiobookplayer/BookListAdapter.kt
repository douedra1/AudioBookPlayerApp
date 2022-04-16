package com.example.myaudiobookplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookListAdapter (var clickedItem: ClickedItem) : RecyclerView.Adapter<BookListAdapter.BookListAdapterVh>(){

    private var bookList = ArrayList<Book>();

    fun setData(bookList: ArrayList<Book>){
        this.bookList = bookList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookListAdapterVh {
        return BookListAdapterVh(
            LayoutInflater.from(parent.context).inflate(R.layout.row_books,parent,false)
        )
    }

    override fun onBindViewHolder(holder: BookListAdapterVh, position: Int) {
        var book = bookList[position]
        var title = book.title
        val author = book.author

        holder.bookTitle.text = title
        holder.bookAuthor.text = author

        holder.itemView.setOnClickListener {
            clickedItem.clickedItem(book)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    interface ClickedItem{
        fun clickedItem(book: Book)
    }

    class BookListAdapterVh(itemView: View): RecyclerView.ViewHolder(itemView) {
        var bookTitle = itemView.findViewById<TextView>(R.id.tvBookName)
        var bookAuthor = itemView.findViewById<TextView>(R.id.tvBookAuthor)

    }


}