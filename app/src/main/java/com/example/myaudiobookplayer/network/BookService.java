package com.example.myaudiobookplayer.network;

import com.example.myaudiobookplayer.database.entity.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookService {

    @GET("cis3515/search.php")
    Call<List<Book>> searchBooks(@Query("term") String term);

}
