package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class WaitArm1ToPos extends Command {

    int pos = 0;
    public WaitArm1ToPos(int pos) {
        this.pos=pos;
        requires(Robot.ballCollectorArm1);
    }

    @Override
    protected void initialize() {
        Robot.ballCollectorArm1.setMotorPos(pos);
    }

    @Override
    protected boolean isFinished() {
       if(Robot.ballCollectorArm1.getMotorPos()>(pos-10)){
            return true;
       }else{
        return false;
       }
    }
}