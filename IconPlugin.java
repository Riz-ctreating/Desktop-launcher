package com.rizal.launcher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.ByteArrayOutputStream;
import java.util.List;

@CapacitorPlugin(name = "IconPlugin")
public class IconPlugin extends Plugin {

  @PluginMethod
  public void getIcon(PluginCall call) {
    String pkg = call.getString("pkg");
    if (pkg == null) {
      call.reject("no pkg");
      return;
    }
    try {
      Drawable d = getContext().getPackageManager().getApplicationIcon(pkg);
      int s = 96;
      Bitmap b = Bitmap.createBitmap(s, s, Bitmap.Config.ARGB_8888);
      Canvas c = new Canvas(b);
      d.setBounds(0, 0, s, s);
      d.draw(c);
      ByteArrayOutputStream o = new ByteArrayOutputStream();
      b.compress(Bitmap.CompressFormat.PNG, 100, o);
      JSObject r = new JSObject();
      r.put("data", "data:image/png;base64," + Base64.encodeToString(o.toByteArray(), Base64.NO_WRAP));
      call.resolve(r);
    } catch (Exception e) {
      call.reject("not found");
    }
  }

  @PluginMethod
  public void getApps(PluginCall call) {
    try {
      PackageManager pm = getContext().getPackageManager();
      Intent i = new Intent(Intent.ACTION_MAIN);
      i.addCategory(Intent.CATEGORY_LAUNCHER);
      List<ResolveInfo> list = pm.queryIntentActivities(i, 0);
      String me = getContext().getPackageName();
      JSArray arr = new JSArray();
      for (ResolveInfo ri : list) {
        String p = ri.activityInfo.packageName;
        if (p.equals(me)) continue;
        JSObject o = new JSObject();
        o.put("label", ri.loadLabel(pm).toString());
        o.put("pkg", p);
        arr.put(o);
      }
      JSObject res = new JSObject();
      res.put("apps", arr);
      call.resolve(res);
    } catch (Exception e) {
      call.reject("gagal");
    }
  }
}
