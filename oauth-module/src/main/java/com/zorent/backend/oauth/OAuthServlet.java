/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.zorent.backend.oauth;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OAuthServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(OAuthServlet.class.getName());
    private static final String [] FAILED = new String[]{"", ""};

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final String accessToken = req.getParameter("accessToken");
        final String provider = req.getParameter("provider");
        if (isEmpty(accessToken) || isEmpty(provider))
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Authorization token");

        logger.info(accessToken);
        logger.info(provider);

        final SynchronousQueue<String[]> waiter = new SynchronousQueue<>();

        final Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {

                try {
                    if (accessToken.equals(authData.getToken()))
                        waiter.put(new String[]{authData.getUid(), authData.getProvider()});
                    else
                        waiter.put(FAILED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

                logger.severe(firebaseError.toString());
                try {
                    waiter.put(FAILED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        //Create a new Firebase instance and subscribe on child events.
        final Firebase firebase = new Firebase("https://flickering-fire-7874.firebaseio.com");
        if (provider.equals("password"))
            firebase.authWithCustomToken(accessToken, authResultHandler);
        else
            firebase.authWithOAuthToken(provider, accessToken, authResultHandler);

        resp.setContentType("text/plain");

        final PrintWriter printWriter = new PrintWriter(resp.getOutputStream());

        try {

            final String [] toWrite = waiter.take();
            printWriter.println(toWrite[0]);
            printWriter.println(toWrite[1]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            firebase.unauth();
        }
    }

    private static boolean isEmpty(CharSequence sequence) {
        return sequence == null || sequence.length() == 0;
    }
}