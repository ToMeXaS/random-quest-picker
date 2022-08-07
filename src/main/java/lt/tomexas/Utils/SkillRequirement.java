package lt.tomexas.Utils;

import lombok.Getter;
import lombok.Setter;
import lt.tomexas.PickerPlugin;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

public class SkillRequirement {

    @Getter
    @Setter
    private String skill;

    @Getter
    @Setter
    private int level;

    @Getter
    @Setter
    private boolean boostable;

    public BufferedImage getIcon() {
        if (this.skill.equalsIgnoreCase("quest points")) {
            return ImageUtil.loadImageResource(PickerPlugin.class, "/quest_point.png");
        } else if (this.skill.equalsIgnoreCase("combat level")) {
            return ImageUtil.loadImageResource(PickerPlugin.class, "/combat_level.png");
        }

        return new SkillIconManager().getSkillImage(Skill.valueOf(skill.toUpperCase()), true);
    }
}
