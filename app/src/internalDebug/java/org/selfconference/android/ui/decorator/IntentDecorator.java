package org.selfconference.ui.decorator;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.StyleSpan;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.selfconference.ui.misc.Truss;
import timber.log.Timber;

public final class IntentDecorator {
  private final Intent intent;

  public static IntentDecorator decorate(Intent intent) {
    return new IntentDecorator(intent);
  }

  private IntentDecorator(Intent intent) {
    this.intent = intent;
  }

  public String action() {
    String action = intent.getAction();
    if (action == null) {
      return "None!";
    }
    return action;
  }

  public String data() {
    Uri data = intent.getData();
    if (data == null) {
      return "None!";
    }
    return data.toString();
  }

  public String extras() {
    Bundle extras = intent.getExtras();
    if (extras == null) {
      return "None!";
    }
    Truss truss = new Truss();
    for (String key : extras.keySet()) {
      Object value = extras.get(key);

      String valueString;
      if (value.getClass().isArray()) {
        valueString = Arrays.toString((Object[]) value);
      } else {
        valueString = value.toString();
      }

      truss.pushSpan(new StyleSpan(Typeface.BOLD));
      truss.append(key).append(":\n");
      truss.popSpan();
      truss.append(valueString).append("\n\n");
    }
    return truss.build().toString();
  }

  public String flags() {
    int flags = intent.getFlags();

    StringBuilder builder = new StringBuilder();
    for (Field field : Intent.class.getDeclaredFields()) {
      try {
        if (field.getName().startsWith("FLAG_")
            && field.getType() == Integer.TYPE
            && (flags & field.getInt(null)) != 0) {
          builder.append(field.getName()).append('\n');
        }
      } catch (IllegalAccessException e) {
        Timber.e(e, "Couldn't read value for: %s", field.getName());
      }
    }

    if (builder.length() == 0) {
      return "None!";
    }
    return builder.toString();
  }
}
