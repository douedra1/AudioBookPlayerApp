package com.example.myaudiobookplayer


import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.example.myaudiobookplayer.database.entity.Book
import com.squareup.picasso.Picasso


class BookDetailsFragment : Fragment() {

    private lateinit var tvClickedBookTitle: TextView
    private lateinit var tvClickedBookAuthor: TextView
    private lateinit var ivBookCover: ImageView
    private lateinit var book: Book;
    private val viewModel: AppViewModel by activityViewModels()

    companion object {

        fun newInstance(book: Book): BookDetailsFragment {
            val fragmentDetails = BookDetailsFragment();
            fragmentDetails.book = book
            return fragmentDetails;
        }
        fun newInstance(): BookDetailsFragment {
            val fragmentDetails = BookDetailsFragment();
            return fragmentDetails;
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.frag_book_details, container, false)
        findView(view)
        return view;
    }

    private fun findView(view: View){
        tvClickedBookTitle = view.findViewById(R.id.tvClickedBookTitle)
        tvClickedBookAuthor = view.findViewById(R.id.tvClickedBookAuthor)
        ivBookCover = view.findViewById(R.id.ivBookCover)

        initData();
    }

    private fun initData(){
        viewModel.selectedBook.observe(viewLifecycleOwner) { book ->
            setBookDate(book)
        }

        if(::book.isInitialized){
            setBookDate(book)
        }

    }

    private fun setBookDate(book: Book){
        tvClickedBookTitle.text = book.title
        tvClickedBookAuthor.text = book.author
        var bookUrl:String = book.cover_url.replace("\\/","");
        Picasso.get().load(bookUrl).into(ivBookCover);
        if(book.localPath == null ||  book.localPath == "") {
            downloadBook()
        }
    }

    private fun downloadBook(){

        Dexter.withContext(requireActivity())
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    Log.e(" download ", " started ")

                    viewModel.downloadBook(book)


                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                }
            }).check()

    }


}