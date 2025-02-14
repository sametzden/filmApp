package com.example.filmapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.filmapp.data.Movie
import com.example.filmapp.data.MovieDetail
import com.example.filmapp.data.TVShow
import com.example.filmapp.data.TVShowDetail
import com.example.filmapp.data.movieForSave
import com.example.filmapp.data.showForSave
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {



    // Film kaydetme
    fun saveMovie(movie: movieForSave) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        println("save movie calıstı "+ userId)
        db.collection("users").document(userId.toString())
            .collection("movies")
            .document(movie.id.toString())
            .set(movie)
            .addOnFailureListener { e ->
                println("movie save olmadı")
                Log.w("Firestore", "Error saving movie", e)
            }
    }

    // Film çıkarma
    fun removeMovie( movieId: Int) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        db.collection("users").document(userId)
            .collection("movies")
            .document(movieId.toString())
            .delete()
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error removing movie", e)
            }
    }

    // TV Şovu kaydetme
    fun saveTvShow( tvShow: showForSave) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        db.collection("users").document(userId)
            .collection("tvShows")
            .document(tvShow.id.toString())
            .set(tvShow)
            .addOnFailureListener { e ->

                Log.w("Firestore", "Error saving TV show", e)
            }
    }

    // TV Şovu çıkarma
    fun removeTvShow( tvShowId: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId)
            .collection("tvShows")
            .document(tvShowId.toString())
            .delete()
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error removing TV show", e)
            }
    }

    // Kaydedilen filmleri dinleme
    fun getSavedMovies( callback: (List<movieForSave>) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val db = FirebaseFirestore.getInstance()
        if (userId != null){
            db.collection("users").document(userId)
                .collection("movies")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        println(e)
                        println("saved movies hatası")
                        Log.w("Firestore", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    val movies = snapshot?.toObjects(movieForSave::class.java) ?: emptyList()
                    callback(movies)
                }
        }

    }

    // Kaydedilen TV şovlarını dinleme
    fun getSavedTvShows( callback: (List<showForSave>) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val db = FirebaseFirestore.getInstance()
        if (userId != null){
            db.collection("users").document(userId)
                .collection("tvShows")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("Firestore", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    val tvShows = snapshot?.toObjects(showForSave::class.java) ?: emptyList()
                    callback(tvShows)
                }
        }

    }
}