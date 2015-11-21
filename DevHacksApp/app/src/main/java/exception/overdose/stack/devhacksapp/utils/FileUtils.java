package exception.overdose.stack.devhacksapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    private  static final String TAG="FileUtils";
    // SD card image directory for profile images
    public static final String PHOTO_ALBUM_PROFILE_IMAGES = "DCIM/Next/ProfileImages";
     // SD card image directory for profile images
    public static final String PHOTO_ALBUM_EVENTS_IMAGES = "DCIM/Next/EventsImages";
    /**
     * Save the bitmap in sd card
     *
     * @param bitmap    The bitmap to be saved
     * @param imageName The name of file
     */
    public static void saveImageToSdCard(Bitmap bitmap, String imageName,int imageUsage) {
        File directory;
        if(imageUsage==Constants.EVENT_IMAGE) {
            directory= getNextEventsImagesDirectory();
        }
        else{
            directory = getNextProfileImagesDirectory();
        }
        // create a File object for the output file
        File outputFile = new File(directory, imageName + ".jpeg");
        // now attach the OutputStream to the file object
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException");
        }
    }
    /**
     * Get the favorites directory
     *
     * @return the favorites directory
     */
    public static File getNextProfileImagesDirectory() {

        File favoritesDirectory = new File(android.os.Environment.getExternalStorageDirectory() +
                File.separator + FileUtils.PHOTO_ALBUM_PROFILE_IMAGES);
        if (!favoritesDirectory.exists()) {
            favoritesDirectory.mkdir();
        }
        return favoritesDirectory;
    }
     /**
     * Get the favorites directory
     *
     * @return the favorites directory
     */
    public static File getNextEventsImagesDirectory() {

        File favoritesDirectory = new File(android.os.Environment.getExternalStorageDirectory() +
                File.separator + FileUtils.PHOTO_ALBUM_EVENTS_IMAGES);
        if (!favoritesDirectory.exists()) {
            favoritesDirectory.mkdir();
        }
        return favoritesDirectory;
    }

    public static Bitmap getImageFromSdCard(String imageName,int imageUsage){
        String directoryPath;
        if(imageUsage==Constants.EVENT_IMAGE) {
            directoryPath= getNextEventsImagesDirectory().getPath();
        }
        else{
             directoryPath = getNextProfileImagesDirectory().getPath();
        }
        return BitmapFactory.decodeFile(directoryPath + File.separator + imageName + ".jpeg");
    }
}