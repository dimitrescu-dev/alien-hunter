package com.mygdx.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mygdx.game.AlienHunter;

// GWT launcher for running Alien Hunter in a web browser
public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Configure the application window size for HTML5
                //return new GwtApplicationConfiguration(true);
                // Fixed size application:
                return new GwtApplicationConfiguration(1280, 720);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                // Return the main game class
                return new AlienHunter();
        }
}

