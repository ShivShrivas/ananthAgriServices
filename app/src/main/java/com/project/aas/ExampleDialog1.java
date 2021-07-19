package com.project.aas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog1 extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setTitle("Terms of service")
                .setMessage("By downloading or using the app, these terms will automatically apply to you – you should make sure therefore that you read them carefully before using the app. You’re not allowed to copy, or modify the app, any part of the app, or our trademarks in any way. You’re not allowed to attempt to extract the source code of the app, and you also shouldn’t try to translate the app into other languages, or make derivative versions. The app itself, and all the trade marks, copyright, database rights and other intellectual property rights related to it, still belong to Ananth Agri Services LLP.\n" +
                        "\n" +
                        "Ananth Agri Services LLP is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you’re paying for.\n" +
                        "\n" +
                        "The ANANTH AGRI SERVICES LLP app stores and processes personal data that you have provided to us, in order to provide our Service. It’s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone’s security features and it could mean that the ANANTH AGRI SERVICES LLP app won’t work properly or at all.\n" +
                        "\n" +
                        "The app does use third party services that declare their own Terms and Conditions.\n" +
                        "\n" +
                        "Link to Terms and Conditions of third party service providers used by the app\n" +
                        "\n" +
                        "Google Play Services\n" +
                        "AdMob\n" +
                        "Google Analytics for Firebase\n" +
                        "Firebase Crashlytics\n" +
                        "Facebook\n" +
                        "Fabric\n" +
                        "Matomo\n" +
                        "Clicky\n" +
                        "Flurry Analytics\n" +
                        "Appodeal\n" +
                        "Fathom Analytics\n" +
                        "Unity\n" +
                        "SDKBOX\n" +
                        "GameAnalytics\n" +
                        "One Signal\n" +
                        "Expo\n" +
                        "Sentry\n" +
                        "AppLovin\n" +
                        "StartApp\n" +
                        "Amplitude\n" +
                        "Adjust\n" +
                        "Mapbox\n" +
                        "Godot\n" +
                        "Segment\n" +
                        "Mixpanel\n" +
                        "You should be aware that there are certain things that Ananth Agri Services LLP will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Wi-Fi, or provided by your mobile network provider, but Ananth Agri Services LLP cannot take responsibility for the app not working at full functionality if you don’t have access to Wi-Fi, and you don’t have any of your data allowance left.\n" +
                        "\n" +
                        "If you’re using the app outside of an area with Wi-Fi, you should remember that your terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third party charges. In using the app, you’re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you’re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.\n" +
                        "\n" +
                        "Along the same lines, Ananth Agri Services LLP cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged – if it runs out of battery and you can’t turn it on to avail the Service, Ananth Agri Services LLP cannot accept responsibility.\n" +
                        "\n" +
                        "With respect to Ananth Agri Services LLP’s responsibility for your use of the app, when you’re using the app, it’s important to bear in mind that although we endeavour to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. Ananth Agri Services LLP accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.\n" +
                        "\n" +
                        "At some point, we may wish to update the app. The app is currently available on Android – the requirements for system(and for any additional systems we decide to extend the availability of the app to) may change, and you’ll need to download the updates if you want to keep using the app. Ananth Agri Services LLP does not promise that it will always update the app so that it is relevant to you and/or works with the Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.\n" +
                        "\n" +
                        "Changes to This Terms and Conditions\n" +
                        "\n" +
                        "We may update our Terms and Conditions from time to time. Thus, you are advised to review this page periodically for any changes. We will notify you of any changes by posting the new Terms and Conditions on this page.\n" +
                        "\n" +
                        "These terms and conditions are effective as of 2021-07-07\n" +
                        "\n" +
                        "Contact Us\n" +
                        "\n" +
                        "If you have any questions or suggestions about our Terms and Conditions, do not hesitate to contact us at info@ananthagriservices.in ")
                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());

        return  builder.create();
    }

}
