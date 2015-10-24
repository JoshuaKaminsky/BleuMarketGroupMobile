package com.mobile.bmg.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mobile.android.common.view.TransparentBusyDialog;
import com.mobile.android.contract.IParamAction;
import com.mobile.bmg.R;

/**
 * Created by Josh on 10/23/15.
 */
public class BaseActivity extends AppCompatActivity {

    private TransparentBusyDialog busyDialog;

    protected void setBusy(boolean show) {
        if(busyDialog != null && busyDialog.isShowing()) {
            busyDialog.dismiss();
        }

        if(show) {
            busyDialog = new TransparentBusyDialog(this, R.drawable.busy_spinner);
            busyDialog.show();
        }
    }

    protected IParamAction responseError = new IParamAction<Void, String>() {
        @Override
        public Void Execute(String error) {
            setBusy(false);

            Toast.makeText(BaseActivity.this, error, Toast.LENGTH_LONG).show();
            return null;
        }
    };
}
