package com.mobile.bmg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobile.android.client.utilities.JsonUtilities;
import com.mobile.android.client.utilities.StringUtilities;
import com.mobile.android.common.CurrencyTextWatcher;
import com.mobile.android.common.FontableTextView;
import com.mobile.android.common.model.ResultCallback;
import com.mobile.android.contract.IParamAction;
import com.mobile.bmg.R;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.common.BmgApp;
import com.mobile.bmg.common.RequestDeleteCart;
import com.mobile.bmg.model.CartItem;
import com.mobile.bmg.model.Organization;
import com.mobile.bmg.model.api.RequestAddItemToCart;
import com.mobile.bmg.model.api.RequestCheckout;
import com.mobile.bmg.model.api.RequestDeleteCartItem;
import com.mobile.bmg.model.api.ResponseAddItemToCart;
import com.mobile.bmg.model.api.ResponseCheckout;
import com.mobile.bmg.model.api.ResponseDeleteCart;
import com.mobile.bmg.model.api.ResponseDeleteCartItem;

import java.text.NumberFormat;
import java.text.ParseException;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by Josh on 10/18/15.
 */
public class DonateActivity extends BaseActivity {

    public static final String OrganizationKey = "OrganizationKey";

    private Context context;

    private Organization organization;

    private boolean donateExtra = true;

    private double amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.donate_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.White));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.context = this;

        this.organization = JsonUtilities.parseJson(getIntent().getStringExtra(DonateActivity.OrganizationKey), Organization.class);

        final FontableTextView donateButton = (FontableTextView)findViewById(R.id.donate_button);

        final EditText otherDonateAmount = (EditText)findViewById(R.id.donate_other_text);

        final RadioGroup donationAmount = (RadioGroup) findViewById(R.id.donate_button_group);

        otherDonateAmount.addTextChangedListener(new CurrencyTextWatcher(otherDonateAmount));

        final CheckBox extraDonate = (CheckBox)findViewById(R.id.donate_extra_donation_check);
        extraDonate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                donateExtra = true;
            }
        });

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBusy(true);

                double actual = 0;

                int radioId = donationAmount.getCheckedRadioButtonId();
                if (radioId == R.id.donate_other_dollars) {
                    String otherAmount = otherDonateAmount.getText().toString();
                    if (StringUtilities.IsNullOrEmpty(otherAmount)) {
                        Toast.makeText(context, "please enter a valid dollar amount", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        actual = NumberFormat.getCurrencyInstance().parse(otherAmount).doubleValue();
                    } catch (ParseException e) {
                        Toast.makeText(context, "please enter a valid dollar amount", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    actual = getAmountFromId(radioId);
                }

                amount = actual;
                BmgApiClient.deleteCart(new RequestDeleteCart(BmgApp.getToken()), new ResultCallback(new IParamAction<Void, ResponseDeleteCart>() {
                    @Override
                    public Void Execute(ResponseDeleteCart item) {
                        BmgApiClient.addItemToCart(
                                new RequestAddItemToCart(organization.id, amount, BmgApp.getToken()),
                                new ResultCallback(addItemToCartSuccess, responseError, ResponseAddItemToCart.class));

                        return null;
                    }
                }, responseError, ResponseDeleteCart.class));


            }
        });

        donationAmount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.donate_other_dollars) {
                    otherDonateAmount.setVisibility(View.VISIBLE);
                    otherDonateAmount.requestFocus();
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.showSoftInput(otherDonateAmount, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    otherDonateAmount.setVisibility(View.GONE);
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(group.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

                donateButton.setEnabled(true);
                donateButton.setBackgroundResource(R.color.bmg_green);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case BmgApp.CreditCardScanRequest:
                if (data == null || !data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                    //error
                    return;
                }

                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                RequestCheckout request = new RequestCheckout(scanResult.cardNumber, scanResult.expiryMonth, scanResult.expiryYear, BmgApp.getToken());

                setBusy(true);

                BmgApiClient.checkout(request, new ResultCallback(checkoutSuccess, responseError, ResponseCheckout.class));
        }

    }

    private IParamAction addItemToCartSuccess = new IParamAction<Void, ResponseAddItemToCart>() {
        @Override
        public Void Execute(ResponseAddItemToCart item) {
            if(item == null) {
                return null;
            }

            if(!donateExtra && item.cart.length > 1) {
                CartItem extraDonation = null;

                for(CartItem cartItem: item.cart) {
                    if(cartItem.price != amount) {
                        extraDonation = cartItem;
                    }
                }

                if(extraDonation != null) {
                    BmgApiClient.deleteCartItem(new RequestDeleteCartItem(extraDonation.id, BmgApp.getToken()), new ResultCallback(removeItemFromCartSuccess, responseError, ResponseDeleteCartItem.class));
                }
            } else {
                setBusy(false);

                openCreditCardReader();
            }

            return null;
        }
    };

    private IParamAction removeItemFromCartSuccess = new IParamAction<Void, ResponseDeleteCartItem>() {
        @Override
        public Void Execute(ResponseDeleteCartItem item) {
            setBusy(false);

            if(item == null) {
                Toast.makeText(context, "an error occurred processing your request.  please try again.", Toast.LENGTH_LONG).show();
                return null;
            }

            openCreditCardReader();

            return null;
        }
    };

    private IParamAction checkoutSuccess = new IParamAction<Void, ResponseCheckout>() {
        @Override
        public Void Execute(ResponseCheckout item) {
            setBusy(false);

            Toast.makeText(context, "your donation has been processed.  thank you.", Toast.LENGTH_LONG).show();

            setResult(RESULT_OK);
            finish();
            return null;
        }
    };

    private void openCreditCardReader() {
        Intent scanIntent = new Intent(context, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, BmgApp.CreditCardScanRequest);
    }

    private double getAmountFromId(int id) {
        switch(id) {
            case R.id.donate_five_dollars:
                return 5;
            case R.id.donate_twenty_five_dollars:
                return 25;
            case R.id.donate_fifty_dollars:
                return 50;
            case R.id.donate_one_hundred_dollars:
                return 100;
            case R.id.donate_two_hundred_fifty_dollars:
                return 250;
            case R.id.donate_five_hundred_dollars:
                return 500;
            default:
                return 0;
        }
    }
}
