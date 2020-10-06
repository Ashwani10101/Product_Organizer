package com.ash.shopadminlogin

import android.content.Context
import android.graphics.*
import java.io.File


class Helper
{
    companion object
    {
        fun getThumbnail(context: Context, uniqueID: String) : Bitmap?
        {
            val imagesDir = File(context.filesDir, "Images")

            val path = File(imagesDir, "$uniqueID.jpg")

            return BitmapFactory.decodeFile(path.toString())
        }

        fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap? {
            val output = Bitmap.createBitmap(
                bitmap.width, bitmap
                    .height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)
            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)
            val roundPx = pixels.toFloat()
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }


    }
}