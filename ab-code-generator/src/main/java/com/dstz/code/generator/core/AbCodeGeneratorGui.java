package com.dstz.code.generator.core;

import javax.swing.*;
import java.awt.*;

public class AbCodeGeneratorGui extends JFrame {

	public AbCodeGeneratorGui() throws HeadlessException {
		setTitle("Code Generator");
		setVisible(true);
		initial();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(AbCodeGeneratorGui::new);
	}

	private void initial() {

	}

}
