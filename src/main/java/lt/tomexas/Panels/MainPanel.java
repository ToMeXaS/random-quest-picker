package lt.tomexas.Panels;

import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.shadowlabel.JShadowedLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainPanel extends JPanel {

    private final JLabel title = new JShadowedLabel();
    private final JLabel description = new JShadowedLabel();

    public MainPanel() {
        setBorder(new EmptyBorder(5, 10, 10, 10));
        setLayout(new BorderLayout());

        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        description.setFont(FontManager.getRunescapeSmallFont());
        description.setForeground(Color.GRAY);
        description.setHorizontalAlignment(SwingConstants.CENTER);

        add(title, BorderLayout.NORTH);
        add(description, BorderLayout.CENTER);

        setVisible(false);
    }

    public void setContent(String title, String description) {
        this.title.setText(title);
        this.description.setText(description);
        setVisible(true);
    }
}
