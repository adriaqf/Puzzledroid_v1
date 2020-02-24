package com.sds.puzzledroid.utils;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FBStorageImg {

    // Returns all images saved inside /img's Firebase Storage folder
    public ArrayList<StorageReference> getAllImgFiles() {
        StorageReference parentRef = FirebaseStorage.getInstance().getReference("img");
        ArrayList<StorageReference> sRImages = new ArrayList<>();

        int i = 1;
        while(i <= 5) {
            try {
                StorageReference imgRef = parentRef.child("img" + i++ + ".jpg");
                sRImages.add(imgRef);
                System.out.println(imgRef);
            } catch (NullPointerException e) {
                break;
            }
        }

        return sRImages;
    }
}
