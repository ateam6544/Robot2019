/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//import com.sun.org.apache.bcel.internal.Const;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.LoadBallCollector;
import frc.robot.commands.MoveTurret;
import frc.robot.commands.ReadyGripper;
import frc.robot.commands.StopArmRollers;
import frc.robot.subsystems.ArmPneumatics;
import frc.robot.subsystems.BallCollectorArm2;
import frc.robot.subsystems.BallCollectorArm1;
import frc.robot.subsystems.DriveSystem;
import frc.robot.subsystems.ElevatorSystem;
import frc.robot.subsystems.GripperSystem;
import frc.robot.subsystems.SlideSystem;
import frc.robot.subsystems.TurretSystem;
import frc.robot.Constants;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static ArmPneumatics arm = new ArmPneumatics();
  public static BallCollectorArm2 ballCollectorArm2 = new BallCollectorArm2();
  public static BallCollectorArm1 ballCollectorArm1 = new BallCollectorArm1();
  public static ElevatorSystem elevatorSystem = new ElevatorSystem();
  public static GripperSystem gripperSystem = new GripperSystem();
  public static SlideSystem slideSystem = new SlideSystem();
  public static TurretSystem turret = new TurretSystem();
  public static DriveSystem drive = new DriveSystem();
  public static OI m_oi;
  public static String loadItem = "none";
  public static boolean continueLoad = false;

  Compressor comp = new Compressor(0);
  Solenoid led = new Solenoid(2);

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    ballCollectorArm1.init();
    ballCollectorArm2.init();
    comp.setClosedLoopControl(true);
    comp.start();
    elevatorSystem.init();
    m_oi = new OI();
    drive.init();
    turret.init();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
    arm.stopRollers();
    ballCollectorArm2.stopMotor();
    ballCollectorArm1.stopMotor();
    elevatorSystem.stopMotor();
    gripperSystem.stopMotor();
    slideSystem.stopMotor();
    turret.stopMotor();
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    led.set(true);
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    SmartDashboard.putBoolean("Forward Limit", Robot.slideSystem.getForwardLimit());
    SmartDashboard.putBoolean("Reverse Limit", Robot.slideSystem.getReverseLimit());
    ballCollectorArm1.log();
    ballCollectorArm2.log();
    turret.log();
    SmartDashboard.putData(Robot.drive._driveBase);
    elevatorSystem.log();

    if(Robot.gripperSystem.getButton()==false){
      Scheduler.getInstance().add(new ReadyGripper());
    }
//human ball position 36037s
    if(Robot.turret.getLeftLimitSwitch()==false){
     int currentPos = Robot.turret.getPosition();
     Constants.turretTurnMinPos = currentPos;
     Constants.turretTurnMaxPos = currentPos+7610;
     Robot.turret.setTurretPos(500);
     Scheduler.getInstance().add(new MoveTurret());
    }
    if(Robot.turret.getRightLimitSwitch()==false){
      int currentPos = Robot.turret.getPosition();
      Constants.turretTurnMinPos = currentPos-7610;
      Constants.turretTurnMaxPos = currentPos;
      Robot.turret.setTurretPos(500);
      Scheduler.getInstance().add(new MoveTurret());
     }
    
        if(Robot.arm.getBallStopButton()==false&&continueLoad==false){
          Scheduler.getInstance().add(new LoadBallCollector());
        }
    }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
