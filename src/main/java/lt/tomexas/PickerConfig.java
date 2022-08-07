package lt.tomexas;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.*;

@ConfigGroup("optimal-quest-guide")
public interface PickerConfig extends Config {

    @ConfigSection(
            name = "Quest Status Colors",
            description = "Colors of not started, in progress and completed Quest",
            position = 98
    )
    String questStatusSection = "status";

    @ConfigSection(
            name = "Quest Requirement Colors",
            description = "Colors of met, unmet and boostable Quest Requirements",
            position = 99
    )
    String questRequirementSection = "requirement";

    @ConfigItem(
            keyName = "inProgressColor",
            name = "Quest in progress color",
            description = "Color of Quests in progress",
            section = questStatusSection
    )
    default Color getInProgressColor() {
        return new Color(240, 207, 123);
    }

    @ConfigItem(
            keyName = "completedColor",
            name = "Quest completed color",
            description = "Color of Quests that have been completed",
            section = questStatusSection
    )
    default Color getCompletedColor() {
        return new Color(110, 225, 110);
    }

    @ConfigItem(
            keyName = "notStartedColor",
            name = "Quest not started color",
            description = "Color of Quests that have not been started",
            section = questStatusSection
    )
    default Color getNotStartedColor() {
        return new Color(198, 198, 198);
    }

    @ConfigItem(
            keyName = "requirementMetColor",
            name = "Requirement level met",
            description = "Color of Requirements that have been met or exceeded",
            section = questRequirementSection
    )
    default Color getRequirementMetColor() {
        return new Color(110, 225, 110);
    }

    @ConfigItem(
            keyName = "requirementUnmetColor",
            name = "Requirement level unmet",
            description = "Color of Requirements that have been unmet and unboostable",
            section = questRequirementSection
    )
    default Color getRequirementUnmetColor() {
        return new Color(230, 30, 30);
    }

    @ConfigItem(
            keyName = "selectedData",
            name = "",
            description = "",
            hidden = true
    )
    default String selectedData()
    {
        return "";
    }

    @ConfigItem(
            keyName = "selectedData",
            name = "",
            description = "",
            hidden = true
    )
    void selectedData(String selectedData);
}
