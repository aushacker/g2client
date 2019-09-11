/*
 * Copyright 2019 Stephen Davies
 *
 * This file is part of g2client.
 *
 * g2client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * g2client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2client. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.aushacker.g2client.jfx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import com.github.aushacker.g2client.conn.IController;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since August 2019
 */
public class CodePane extends G2Pane<BorderPane> {

	private Button btPlay;

	private CodeArea codeArea;

	public CodePane(Stage top, IController controller, UIPreferences preferences) {
		super(top, controller, preferences);
		
		initialize();
	}

	@Override
	protected void createWidgets() {
		btPlay = new Button("Play");
		codeArea = new CodeArea("");

		// add line numbers to the left of area
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		//codeArea.setEditable(false);
	}

	@Override
	protected void hookEvents() {
		btPlay.setOnAction(e -> play());
	}

	@Override
	protected void initializePane() {
		setPane(new BorderPane());
	}

	@Override
	protected void layoutWidgets() {
		HBox buttons = new HBox();
		buttons.getChildren().add(btPlay);
		
		getPane().setTop(buttons);
		getPane().setCenter(codeArea);
	}

	/**
	 * Dumps the contents of a file into the code pane. Hopefully this is
	 * actually a gcode file.
	 */
	public void openFile(File gcode) {
		try (BufferedReader in = new BufferedReader(new FileReader(gcode))) {
			StringBuilder sb = new StringBuilder();
			String line = in.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = in.readLine();
			}

			codeArea.replaceText(sb.toString());
			codeArea.moveTo(0, 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void play() {
		codeArea.moveTo(0, 0);
		getController().resetLineCounter();
		
		String[] lines = codeArea.getText().split("\\n");
		for (int i = 0; i < lines.length; i++) {
			String c = "N" + (i + 1) + " " + lines[i];
			getController().enqueue(c);
		}
	}
}
