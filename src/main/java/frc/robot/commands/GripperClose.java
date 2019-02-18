package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

public class GripperClose extends InstantCommand {
    @Override
    protected void execute() {
        Robot.gripperSystem.closeGripper();
    }
}