/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.myapplication.ARcore.rendering;

import android.content.Context;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;

import java.io.IOException;

/** Renders an augmented image. */
public class AugmentedImageRenderer {
  private static final String TAG = "AugmentedImageRenderer";

  private static final float TINT_INTENSITY = 0.1f;
  private static final float TINT_ALPHA = 1.0f;
  private static final int[] TINT_COLORS_HEX = {
    0x000000, 0xF44336, 0xE91E63, 0x9C27B0, 0x673AB7, 0x3F51B5, 0x2196F3, 0x03A9F4, 0x00BCD4,
    0x009688, 0x4CAF50, 0x8BC34A, 0xCDDC39, 0xFFEB3B, 0xFFC107, 0xFF9800,
  };

  private final ObjectRenderer imageFrameUpperRight = new ObjectRenderer();


  public void createOnGlThread(Context context, String textureName) throws IOException {

    Log.d("texture", textureName);
    imageFrameUpperRight.createOnGlThread(
            context,
            "models/andy_shadow2.obj",
            textureName

    );
    imageFrameUpperRight.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
    imageFrameUpperRight.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending);
  }

  public void draw(
      float[] viewMatrix,
      float[] projectionMatrix,
      AugmentedImage augmentedImage,
      Anchor centerAnchor,
      float[] colorCorrectionRgba) {
    float[] tintColor =
        convertHexToColor(TINT_COLORS_HEX[augmentedImage.getIndex() % TINT_COLORS_HEX.length]);

    final float mazeEdgeSize = 492.65f;
    final float maxImageEdgeSize = Math.max(augmentedImage.getExtentX(), augmentedImage.getExtentZ());
    float mazeScaleFactor = maxImageEdgeSize / mazeEdgeSize;
    Pose mazeModelLocalOffset = Pose.makeTranslation(
            -251.3f * mazeScaleFactor,
            0.0f,
            129.0f * mazeScaleFactor
    );

    //必要
    Pose anchorPose = centerAnchor.getPose();
    float[] modelMatrix = new float[16];

    Pose[] localBoundaryPoses = {
      Pose.makeTranslation(
          0.0f * augmentedImage.getExtentX(),
          0.0f,
          0.0f * augmentedImage.getExtentZ()
      ), // upper left
    };

    Pose localBoundaryPose = Pose.makeTranslation(
            0.0f * augmentedImage.getExtentX(),
            0.0f,
            0.0f * augmentedImage.getExtentZ()
    );


    Pose[] worldBoundaryPoses = new Pose[1];
    for (int i = 0; i < 1; ++i) {
      worldBoundaryPoses[i] = anchorPose.compose(localBoundaryPoses[i]);
    }

    float scaleFactor = 1.0f;

    //worldBoundaryPoses[0].toMatrix(modelMatrix, 0);
   anchorPose.compose(localBoundaryPose).toMatrix(modelMatrix, 0);
    imageFrameUpperRight.updateModelMatrix(modelMatrix, scaleFactor);
    imageFrameUpperRight.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);

  }

  private static float[] convertHexToColor(int colorHex) {
    // colorHex is in 0xRRGGBB format
    float red = ((colorHex & 0xFF0000) >> 16) / 255.0f * TINT_INTENSITY;
    float green = ((colorHex & 0x00FF00) >> 8) / 255.0f * TINT_INTENSITY;
    float blue = (colorHex & 0x0000FF) / 255.0f * TINT_INTENSITY;
    return new float[] {red, green, blue, TINT_ALPHA};
  }
}
