/*
 * This file is part of Email4n6.
 * Copyright (C) 2018  Marten4n6
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.email4n6.view.tabs.search;

import com.github.email4n6.view.messagepane.MessagePane;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Tab implementation which provides a simple way to search through messages.
 *
 * @author Marten4n6
 */
@Slf4j
public class SearchTab {

    private @Getter Tab tab;
    private @Getter MessagePane messagePane;

    private TextField searchField;

    // Listeners
    private @Setter EventHandler<ActionEvent> onSearch;
    private @Setter EventHandler<MouseEvent> onSettingsClicked;

    /**
     * Initializes the search tab.
     */
    public SearchTab() {
        tab = new Tab();
        BorderPane borderPane = new BorderPane();
        messagePane = new MessagePane();

        // Border Pane
        borderPane.setPadding(new Insets(5, 0, 0, 0));

        // Tab
        tab.setText("Search");
        tab.setClosable(false);
        tab.setContent(borderPane);
        tab.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/images/search.png"))));

        // Search field
        HBox topLayout = new HBox();

        topLayout.setSpacing(3);
        topLayout.setMaxWidth(Double.MAX_VALUE);

        searchField = new TextField();
        Label settingsLabel = new Label();
        ImageView settingsIcon = new ImageView(new Image(this.getClass().getResourceAsStream("/images/settings.png")));

        settingsLabel.setGraphic(settingsIcon);
        settingsLabel.setCursor(Cursor.HAND);

        HBox.setHgrow(searchField, Priority.ALWAYS);
        topLayout.getChildren().addAll(searchField, settingsLabel);
        BorderPane.setMargin(topLayout, new Insets(5, 0, 5, 5));

        // Listeners
        searchField.setOnAction((event) -> onSearch.handle(event));
        settingsLabel.setOnMouseClicked((event) -> onSettingsClicked.handle(event));

        // Add
        borderPane.setTop(topLayout);
        borderPane.setCenter(messagePane.getPane());
    }

    /**
     * @return The entered search query.
     */
    String getSearchQuery() {
        return searchField.getText();
    }

    /**
     * Reloads the search query.
     */
    void reload() {
        searchField.fireEvent(new ActionEvent());
    }

    public void setLoading(boolean loading) {
        Platform.runLater(() -> {
            if (loading) {
                searchField.setCursor(Cursor.WAIT);
                messagePane.getTable().setCursor(Cursor.WAIT);
            } else {
                searchField.setCursor(Cursor.TEXT);
                messagePane.getTable().setCursor(Cursor.DEFAULT);
            }
        });
    }
}
