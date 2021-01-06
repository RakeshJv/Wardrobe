package  com.wardrobe.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.wardrobe.fragment.BaseFragment
import  com.wardrobe.BuildConfig
import  com.wardrobe.R
import  com.wardrobe.data_layer.DataRepo
import com.wardrobe.events.BottomAddedEvent
import com.wardrobe.events.TopAddedEvent
import com.wardrobe.factory.getImageCaptureIntent
import com.wardrobe.factory.getImagePickIntent
import com.wardrobe.model.BottomElement
import com.wardrobe.model.TopElement
import com.wardrobe.model.WardrobeType
import com.wardrobe.util.*
import java.io.File
import java.util.*

/**
 * Base class for Media handling - Click image / Pick from gallery
 */
abstract class BasePickerFragment : BaseFragment() {

    override val fragmentTag = TAG

    companion object {
        private const val TAG = "BasePickerFragment"

        private const val CAPTURE_IMAGE_REQUEST_CODE = 1001
        private const val PICK_PHOTO_REQUEST_CODE = 1002

        private const val REQUEST_EXTERNAL_STORAGE_READ_PERMISSION = 2001

        private const val FILE_PROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.fileprovider"

        private const val SAVE_WARDROBE_TYPE = "save_wardrobe_type"
        private const val SAVE_WARDROBE_ID = "save_wardrobe_id"
        private const val SAVE_WARDROBE_FILE = "save_wardrobe_file"
    }

    private var wardrobeType: WardrobeType = WardrobeType.TOP
    private var wardrobeId: String? = null
    private var wardrobeFilePath: String? = null

    /**
     * Show an Alert Dialog to let the user click an image / pick one from Gallery.
     *
     * @param wardrobeType The type of Wardrobe.
     */
    fun showImageSourceDialog(wardrobeType: WardrobeType) {
        AlertDialog.Builder(context!!).apply {
            setTitle(R.string.add_image_title)
            setItems(resources.getTextArray(R.array.image_source_array), { _, which ->
                when (which) {
                    0 -> captureImageWithCamera(wardrobeType)
                    1 -> pickImageFromGallery(wardrobeType)
                }
            })
        }.show()
    }

    /**
     * Capture an image using another Camera application.
     */
    private fun captureImageWithCamera(wardrobeType: WardrobeType) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Try to resolve an activity for taking the Image.
        if (cameraIntent.resolveActivity(activity!!.packageManager).isNull()) {
            Toast.makeText(context, R.string.error_no_camera_app, Toast.LENGTH_LONG).show()
            return
        }

        this.wardrobeType = wardrobeType
        val imageFile = buildFileForImageAndSetupPaths()

        // Get the Uri for image to allow camera write
        var imageUri : Uri? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            var      imageUri =  FileProvider.getUriForFile(activity!!, BuildConfig.APPLICATION_ID + ".fileprovider", imageFile)
//            var intent:Intent = getImageCaptureIntent(imageUri);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(getImageCaptureIntent(imageUri), CAPTURE_IMAGE_REQUEST_CODE)
        };
        // Build the Intent and start the camera
    }

    /**
     * Pick an image using a Gallery / FileManager application.
     */
    private fun pickImageFromGallery(wardrobeType: WardrobeType) {
        this.wardrobeType = wardrobeType
        if (!hasStorageAccess(context!!)) {
            requestStoragePermission(this, REQUEST_EXTERNAL_STORAGE_READ_PERMISSION)
            return
        }
        internalPickFromGallery()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) restoreFromState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVE_WARDROBE_TYPE, wardrobeType)
        outState.putString(SAVE_WARDROBE_ID, wardrobeId)
        outState.putString(SAVE_WARDROBE_FILE, wardrobeFilePath)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            handleAddedItem()
            return
        }
        if (requestCode == PICK_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            handleImagePicked(data!!)
            return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE_READ_PERMISSION &&
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            internalPickFromGallery()
        }
    }

    private fun restoreFromState(inState: Bundle) {
        wardrobeType = inState.getSerializable(SAVE_WARDROBE_TYPE) as WardrobeType
        wardrobeId = inState.getString(SAVE_WARDROBE_ID)
        wardrobeFilePath = inState.getString(SAVE_WARDROBE_FILE)
    }

    private fun handleAddedItem() {
        when (wardrobeType) {

            WardrobeType.TOP -> {
                val topElement = TopElement(wardrobeId!!, wardrobeFilePath!!)
                DataRepo.addTop(topElement)
                Events.postSticky(TopAddedEvent(topElement))
            }

            WardrobeType.BOTTOM -> {
                val bottomElement = BottomElement(wardrobeId!!, wardrobeFilePath!!)
                DataRepo.addBottom(bottomElement)
                Events.postSticky(BottomAddedEvent(bottomElement))
            }
        }
    }

    private fun handleImagePicked(intent: Intent) {
        if (intent.data == null) return

        val imageFile = buildFileForImageAndSetupPaths()
        imageFile.writeToFile(context?.contentResolver?.openInputStream(intent.data!!)!!)

        handleAddedItem()
    }

    private fun buildFileForImageAndSetupPaths(): File {
        wardrobeId = getRandomId()
        val imageFile = createImageFile(context!!, wardrobeId!!)
        wardrobeFilePath = imageFile.absolutePath
        return imageFile
    }

    private fun internalPickFromGallery() {
        val galleryIntent = getImagePickIntent()
        startActivityForResult(Intent.createChooser(galleryIntent, getString(R.string.add_image_pick_chooser_title)), PICK_PHOTO_REQUEST_CODE)
    }
}
