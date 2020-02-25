package com.sds.puzzledroid.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sds.puzzledroid.adapters.ItemClassificationLVAdapter;
import com.sds.puzzledroid.pojos.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FBFirestore {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userName = auth.getCurrentUser().getDisplayName();
    private String userEmail = auth.getCurrentUser().getEmail();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference uDocReference = db.collection("users").document(userEmail);
    private DocumentReference sDocReference = uDocReference.collection("scores").document();

    private List<Score> scores = Collections.synchronizedList(new ArrayList<Score>());
    private List<String> userNames = Collections.synchronizedList(new ArrayList<String>());
    private List<String> usersId = Collections.synchronizedList(new ArrayList<String>());
    private List<Score> bestscores = Collections.synchronizedList(new ArrayList<Score>());

    public String getUserName() {
        return userName;
    }

    // If the user is not found in the Firestore, it's created a new user's document
    public void prepareUserDoc() {
        uDocReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(!documentSnapshot.exists()) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", userName);
                            uDocReference
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("[SUCCESS]", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("[ERROR]", "Error writing document", e);
                                        }
                                    });
                        }
                    }
                });
    }

    // Generates a new score document inside a user document
    public void addScoreDoc(Score score) {
        Map<String, Object> mScore = new HashMap<>();
        mScore.put("seconds", score.getTotalScore());
        mScore.put("difficulty", score.getDifficulty());

        sDocReference
                .set(mScore)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("[SUCCESS]", "Score successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("[ERROR]", "Error writing document", e);
                    }
                });
    }

    // Gets all user names and their ids inside Firestore
    private void getAllUserNames() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                userNames.add(documentSnapshot.get("name").toString());
                                usersId.add(documentSnapshot.getId());
                            }
                            Log.d("[USER NAME SUCCESS]","Getting user name successfully done.");
                        }
                        else {
                            Log.d("[USER NAME ERROR]", "Task not successful.");
                        }
                    }
                });
    }

    // Gets all scores of all user by their ids
    private void getAllUsersScores() {
        for(int i = 0; i < usersId.size(); i++) {
            final int finalI = i;
            db.collection("users/" + usersId.get(i) + "/scores")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Score score = new Score();
                                    score.setTotalScore((int)(long) documentSnapshot.get("seconds"));
                                    score.setDifficulty((int)(long) documentSnapshot.get("difficulty"));
                                    score.setUserName(userNames.get(finalI));

                                    scores.add(score);
                                }
                                Log.d("[SCORES SUCCESS]", "Getting scores successfully done.");
                            } else {
                                Log.d("[SCORES ERROR]", "There was a problem getting scores.");
                            }
                        }

                    });

        }
    }

    // Sort the array list collection by getting best 10 scores
    private void getBestTenScores() {
        Collections.sort(scores);
        try {
            for(int i = 0; i < 10; i++) {
                bestscores.add(scores.get(i));
            }
            Log.d("[TOP 10 SUCCESS]", "Sorting successfully done.");
        } catch (Exception e) {
            Log.d("[TOP TEN ERROR]", "No more items found.");
        }

    }

    public void getTopTenScores(final Context context, final ListView listView, final ProgressDialog progress) {
        new Runnable() {
            @Override
            public void run() {
                getAllUserNames();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAllUsersScores();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getBestTenScores();
                                ItemClassificationLVAdapter itemLVAdapter = new ItemClassificationLVAdapter(context, bestscores);
                                listView.setAdapter(itemLVAdapter);
                                progress.dismiss();
                            }
                        }, 1000);
                        getBestTenScores();
                        ItemClassificationLVAdapter itemLVAdapter = new ItemClassificationLVAdapter(context, bestscores);
                        listView.setAdapter(itemLVAdapter);
                    }
                }, 1000);
            }
        }.run();

    }

}

