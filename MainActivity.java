package com.rizal.launcher;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    registerPlugin(IconPlugin.class);
    super.onCreate(savedInstanceState);
  }
}
