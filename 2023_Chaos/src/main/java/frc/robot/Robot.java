package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.motorcontrol.Victor;



import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

// import javax.lang.model.util.ElementScanner6;

// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  private Joystick m_leftStick;
  private Joystick m_rightStick;
  private Joystick m_joystick;
  private Joystick m_joystick_kol;
  //  private static final int kalkmotorno2 = 8;
  // private static final int frontLeftDeviceID = 4;
  private static final int kalkmotorno1 = 11;
  // private static final int frontRightDeviceID = 3;
  // private static final int kalkmotorno1 = 9;

  // private CANSparkMax m_alis; // Eski sol arka moto r m_rearLeftMotor
  // private CANSparkMax m_frontLeftMotor;
  // private CANSparkMax m_frontRightMotor;
  // private CANSparkMax m_atis; // Eski sağ arka motor m_rearRightMotor
  private double startTime;

  DigitalInput m_limitSwitch_upper;
  DigitalInput m_limitSwitch_lower;
  
  double time = Timer.getFPGATimestamp();
  boolean before;

  // private final Compressor comp = new Compressor();
  // private final DoubleSolenoid solenoid = new DoubleSolenoid(0,1);
  Compressor comp = new Compressor(0, PneumaticsModuleType.CTREPCM);
  DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
  DoubleSolenoid solenoid2 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 3);


  TalonSRX m_talon_rearRight = new TalonSRX(5);
  TalonSRX m_talon_frontRight = new TalonSRX(15);
  TalonSRX m_talon_frontLeft = new TalonSRX(12);
  TalonSRX m_talon_rearLeft = new TalonSRX(10);
  VictorSPX kalkmotor1 = new VictorSPX(kalkmotorno1);
  // VictorSPX kalkmotor2 = new VictorSPX(kalkmotorno2);




  @Override
  public void robotInit() {

    // m_alis = new CANSparkMax(rearLeftDeviceID, MotorType.kBrushless);
    // m_frontLeftMotor = new CANSparkMax(frontLeftDeviceID, MotorType.kBrushed);
    // m_frontRightMotor = new CANSparkMax(frontRightDeviceID, MotorType.kBrushed);
    // // m_atis = new CANSparkMax(rearRightDeviceID, MotorType.kBrushed);

    // // m_alis.restoreFactoryDefaults();
    // m_frontLeftMotor.restoreFactoryDefaults();
    // // m_atis.restoreFactoryDefaults();
    // m_frontRightMotor.restoreFactoryDefaults();

    // m_rearLeftMotor.follow(m_frontLeftMotor);
    // m_rearRightMotor.follow(m_frontRightMotor);

    // m_myRobot = new DifferentialDrive(m_frontRightMotor, m_frontLeftMotor);

    m_leftStick = new Joystick(0);
    m_rightStick = new Joystick(0);
    m_joystick = new Joystick(0);
    m_joystick_kol = new Joystick(1);

    m_limitSwitch_upper = new DigitalInput(0); 
    m_limitSwitch_lower = new DigitalInput(1);

  }

  @Override
  public void teleopPeriodic() {
    Surus();
    basinc();
    // Atis();
    Kalkis();

  }

  boolean ilkmi = true;

  private void basinc() {
    if (ilkmi) {
      ilkmi = false;
      comp.disable(); // Compressor elektrik kesiliyor
    } else if (m_joystick_kol.getRawButton(8)) {
      comp.enableDigital(); // Compressor elektrik veriliyor
    } else if (m_joystick_kol.getRawButton(3)) {
      solenoid.set(DoubleSolenoid.Value.kReverse); // Pistonları kapatıyor
    } else if (m_joystick_kol.getRawButton(7)) {
      comp.disable();
    } 
    else if(m_joystick_kol.getRawButton(4)){
solenoid2.set(DoubleSolenoid.Value.kForward);

    }
    else if(m_joystick_kol.getRawButton(6)){
      solenoid2.set(DoubleSolenoid.Value.kReverse);
      
          }
    else if (m_joystick_kol.getRawButton(5)) {
      solenoid.set(DoubleSolenoid.Value.kForward); // Pistonları açıyor
    } else if (m_joystick_kol.getRawButton(10)) {
      solenoid.set(DoubleSolenoid.Value.kOff);
    }

  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    // startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void autonomousPeriodic() {
    Autonomous();
  }

  private void Kalkis() {
    // TODO: Atis'in yanina bir de geri alma yapılacak, fonksiyon adı "kus" olacak
    // <-- !!
    boolean state_upper = m_limitSwitch_upper.get(); 
    boolean state_lower = m_limitSwitch_lower.get();

    if ((m_joystick_kol.getRawButton(1)) && (state_lower == false)) {
      kalkmotor1.set(ControlMode.PercentOutput, -0.45);
    // kalkmotor2.set(ControlMode.PercentOutput, -1);
    
    } else if ((m_joystick_kol.getRawButton(2)) && (state_upper == true)) {
      kalkmotor1.set(ControlMode.PercentOutput, 0.45);

    } else {
      kalkmotor1.set(ControlMode.PercentOutput, 0);
    }

  }

  private void Surus() {
    // m_myRobot.arcadeDrive(m_leftStick.getRawAxis(1), m_rightStick.getRawAxis(0));
    double donushizi = m_leftStick.getRawAxis(0);
    double hiz = m_leftStick.getRawAxis(1);
    double mevlana = m_leftStick.getRawAxis(2);


    System.out.println("x " + donushizi);
    System.out.println("y " + hiz);
    System.out.println("z " + mevlana);

    double sinir = 0.09;

    // // Ileri sag (hiz > sinir) ve geri sag (hiz < -sinir)
    // if (((hiz < -sinir) || (hiz > sinir)) && (donushizi > sinir)) {
    //   m_frontLeftMotor.set(hiz / 0.75);
    //   m_talon_rearLeft.set(ControlMode.PercentOutput, hiz / 0.75);
    //   m_frontRightMotor.set(hiz / -2.5);
    //   m_talon_rearRight.set(ControlMode.PercentOutput, hiz / -2.5);
    // }

    // // Ileri sol (hiz > sinir) ve geri sol (hiz < -sinir)
    // if (((hiz < -sinir) || (hiz > sinir)) && (donushizi < -sinir)) {
    //   m_frontLeftMotor.set(hiz / -2.5);
    //   m_talon_rearLeft.set(ControlMode.PercentOutput, hiz / -2.5);
    //   m_frontRightMotor.set(hiz / 0.75);
    //   m_talon_rearRight.set(ControlMode.PercentOutput, hiz / 0.75);
    // }

    // Kendi ekseninde donme
    if (((donushizi < -sinir) || (donushizi > sinir)) && ((hiz < sinir) && (hiz > -sinir))) {
      // m_frontLeftMotor.set(-donushizi);
      m_talon_rearLeft.set(ControlMode.PercentOutput, donushizi);
      m_talon_frontLeft.set(ControlMode.PercentOutput, donushizi);
      m_talon_rearRight.set(ControlMode.PercentOutput, -donushizi);
      m_talon_frontRight.set(ControlMode.PercentOutput, -donushizi);

    }

    // // Sağ Gitme
    // if (((hiz > sinir) || (hiz < -sinir)) && (donushizi > sinir)) {
    //   m_frontLeftMotor.set(hiz);
    //   m_talon_rearLeft.set(ControlMode.PercentOutput, -hiz);
    //   m_frontRightMotor.set(hiz * donushizi);
    //   m_talon_rearRight.set(ControlMode.PercentOutput, -hiz * donushizi);
    // }

    // // Sol Gitme
    // if (((hiz > sinir) || (hiz < -sinir)) && (donushizi < -sinir)){
    //   m_frontLeftMotor.set(hiz * -donushizi);
    //   m_talon_rearLeft.set(ControlMode.PercentOutput, -hiz * -donushizi);
    //   m_frontRightMotor.set(hiz);
    //   m_talon_rearRight.set(ControlMode.PercentOutput, -hiz); 
    // }

    // Duz gitme
    if (((hiz > sinir) || (hiz < -sinir)) && ((donushizi < sinir) && (donushizi > -sinir))) {
      m_talon_frontLeft.set(ControlMode.PercentOutput, -hiz);
      m_talon_rearLeft.set(ControlMode.PercentOutput, -hiz);
      m_talon_frontRight.set(ControlMode.PercentOutput, -hiz);
      m_talon_rearRight.set(ControlMode.PercentOutput, -hiz);
    
    }
    // // Sağ-Sol Gitmeme
    // if (((donushizi > sinir) || (donushizi < -sinir)) && ((hiz > -sinir) && (hiz
    // < sinir)))
    // m_frontLeftMotor.set(hiz * 0);
    // m_rearLeftMotor.set(hiz * 0);
    // m_frontRightMotor.set(hiz * 0);
    // m_rearRightMotor.set(hiz * 0);

    // Durma
    // if (((hiz < sinir) && (hiz > -sinir)) && ((donushizi < sinir) && (donushizi >
    // -sinir)) && ((mevlana < sinir) && (mevlana > -sinir))) {
    // // Changes might be required here...
    // Double slow = Math.pow(m_leftStick.getRawAxis(1), 3);
    // // System.out.println(slow);
    // m_frontLeftMotor.set(slow);
    // m_rearLeftMotor.set(slow);
    // m_frontRightMotor.set(slow);
    // m_rearRightMotor.set(slow);
    // }
    if ((((hiz < sinir) && (hiz > -sinir)) && ((donushizi < sinir) && (donushizi > -sinir))
        && ((mevlana < sinir) && (mevlana > -sinir)))) {
      m_talon_frontLeft.set(ControlMode.PercentOutput, 0);
      m_talon_rearLeft.set(ControlMode.PercentOutput, 0);
      m_talon_frontRight.set(ControlMode.PercentOutput, 0);
      m_talon_rearRight.set(ControlMode.PercentOutput, 0);

    }
  }

  // int sayac = 0;
  // boolean ilkmi2 = true;

  private void Autonomous() {

  //   sayac++;
  //   if (ilkmi2) {
  //     // comp.disable();
  //     comp.enableDigital();
  //     solenoid.set(DoubleSolenoid.Value.kForward);
  //     ilkmi2 = false;
  //   }

  //   if (sayac > 150 && sayac < 200) {
  //     m_frontLeftMotor.set(0.5);
  //     m_talon_rearLeft.set(ControlMode.PercentOutput, 0.5);
  //     m_frontRightMotor.set(-0.5);
  //     m_talon_rearRight.set(ControlMode.PercentOutput, -0.5);
  //   }

  //   if (sayac > 250 && sayac < 500) {
  //     // mytalon.set(ControlMode.PercentOutput, -1);
  //   }

  //   if (sayac > 500 && sayac < 505) {
  //     comp.disable();
  //     solenoid.set(DoubleSolenoid.Value.kReverse);
  //   }

  //   if (sayac > 550 && sayac < 620) {
  //     // mytalon.set(ControlMode.PercentOutput, 0);
  //     m_frontLeftMotor.set(0.5);
  //     m_talon_rearLeft.set(ControlMode.PercentOutput, 0.5);
  //     m_frontRightMotor.set(-0.5);
  //     m_talon_rearRight.set(ControlMode.PercentOutput, -0.5);
  //   }

  }
}package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.motorcontrol.Victor;



import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

// import javax.lang.model.util.ElementScanner6;

// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  private Joystick m_leftStick;
  private Joystick m_rightStick;
  private Joystick m_joystick;
  private Joystick m_joystick_kol;
  //  private static final int kalkmotorno2 = 8;
  // private static final int frontLeftDeviceID = 4;
  private static final int kalkmotorno1 = 11;
  // private static final int frontRightDeviceID = 3;
  // private static final int kalkmotorno1 = 9;

  // private CANSparkMax m_alis; // Eski sol arka moto r m_rearLeftMotor
  // private CANSparkMax m_frontLeftMotor;
  // private CANSparkMax m_frontRightMotor;
  // private CANSparkMax m_atis; // Eski sağ arka motor m_rearRightMotor
  private double startTime;

  DigitalInput m_limitSwitch_upper;
  DigitalInput m_limitSwitch_lower;
  
  double time = Timer.getFPGATimestamp();
  boolean before;

  // private final Compressor comp = new Compressor();
  // private final DoubleSolenoid solenoid = new DoubleSolenoid(0,1);
  Compressor comp = new Compressor(0, PneumaticsModuleType.CTREPCM);
  DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
  DoubleSolenoid solenoid2 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 3);


  TalonSRX m_talon_rearRight = new TalonSRX(5);
  TalonSRX m_talon_frontRight = new TalonSRX(15);
  TalonSRX m_talon_frontLeft = new TalonSRX(12);
  TalonSRX m_talon_rearLeft = new TalonSRX(10);
  VictorSPX kalkmotor1 = new VictorSPX(kalkmotorno1);
  // VictorSPX kalkmotor2 = new VictorSPX(kalkmotorno2);




  @Override
  public void robotInit() {

    // m_alis = new CANSparkMax(rearLeftDeviceID, MotorType.kBrushless);
    // m_frontLeftMotor = new CANSparkMax(frontLeftDeviceID, MotorType.kBrushed);
    // m_frontRightMotor = new CANSparkMax(frontRightDeviceID, MotorType.kBrushed);
    // // m_atis = new CANSparkMax(rearRightDeviceID, MotorType.kBrushed);

    // // m_alis.restoreFactoryDefaults();
    // m_frontLeftMotor.restoreFactoryDefaults();
    // // m_atis.restoreFactoryDefaults();
    // m_frontRightMotor.restoreFactoryDefaults();

    // m_rearLeftMotor.follow(m_frontLeftMotor);
    // m_rearRightMotor.follow(m_frontRightMotor);

    // m_myRobot = new DifferentialDrive(m_frontRightMotor, m_frontLeftMotor);

    m_leftStick = new Joystick(0);
    m_rightStick = new Joystick(0);
    m_joystick = new Joystick(0);
    m_joystick_kol = new Joystick(1);

    m_limitSwitch_upper = new DigitalInput(0); 
    m_limitSwitch_lower = new DigitalInput(1);

  }

  @Override
  public void teleopPeriodic() {
    Surus();
    basinc();
    // Atis();
    Kalkis();

  }

  boolean ilkmi = true;

  private void basinc() {
    if (ilkmi) {
      ilkmi = false;
      comp.disable(); // Compressor elektrik kesiliyor
    } else if (m_joystick_kol.getRawButton(8)) {
      comp.enableDigital(); // Compressor elektrik veriliyor
    } else if (m_joystick_kol.getRawButton(3)) {
      solenoid.set(DoubleSolenoid.Value.kReverse); // Pistonları kapatıyor
    } else if (m_joystick_kol.getRawButton(7)) {
      comp.disable();
    } 
    else if(m_joystick_kol.getRawButton(4)){
solenoid2.set(DoubleSolenoid.Value.kForward);

    }
    else if(m_joystick_kol.getRawButton(6)){
      solenoid2.set(DoubleSolenoid.Value.kReverse);
      
          }
    else if (m_joystick_kol.getRawButton(5)) {
      solenoid.set(DoubleSolenoid.Value.kForward); // Pistonları açıyor
    } else if (m_joystick_kol.getRawButton(10)) {
      solenoid.set(DoubleSolenoid.Value.kOff);
    }

  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    // startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void autonomousPeriodic() {
    Autonomous();
  }

  private void Kalkis() {
    // TODO: Atis'in yanina bir de geri alma yapılacak, fonksiyon adı "kus" olacak
    // <-- !!
    boolean state_upper = m_limitSwitch_upper.get(); 
    boolean state_lower = m_limitSwitch_lower.get();

    if ((m_joystick_kol.getRawButton(1)) && (state_lower == false)) {
      kalkmotor1.set(ControlMode.PercentOutput, -0.45);
    // kalkmotor2.set(ControlMode.PercentOutput, -1);
    
    } else if ((m_joystick_kol.getRawButton(2)) && (state_upper == true)) {
      kalkmotor1.set(ControlMode.PercentOutput, 0.45);

    } else {
      kalkmotor1.set(ControlMode.PercentOutput, 0);
    }

  }

  private void Surus() {
    // m_myRobot.arcadeDrive(m_leftStick.getRawAxis(1), m_rightStick.getRawAxis(0));
    double donushizi = m_leftStick.getRawAxis(0);
    double hiz = m_leftStick.getRawAxis(1);
    double mevlana = m_leftStick.getRawAxis(2);


    System.out.println("x " + donushizi);
    System.out.println("y " + hiz);
    System.out.println("z " + mevlana);

    double sinir = 0.09;

    // // Ileri sag (hiz > sinir) ve geri sag (hiz < -sinir)
    // if (((hiz < -sinir) || (hiz > sinir)) && (donushizi > sinir)) {
    //   m_frontLeftMotor.set(hiz / 0.75);
    //   m_talon_rearLeft.set(ControlMode.PercentOutput, hiz / 0.75);
    //   m_frontRightMotor.set(hiz / -2.5);
    //   m_talon_rearRight.set(ControlMode.PercentOutput, hiz / -2.5);
    // }

    // // Ileri sol (hiz > sinir) ve geri sol (hiz < -sinir)
    // if (((hiz < -sinir) || (hiz > sinir)) && (donushizi < -sinir)) {
    //   m_frontLeftMotor.set(hiz / -2.5);
    //   m_talon_rearLeft.set(ControlMode.PercentOutput, hiz / -2.5);
    //   m_frontRightMotor.set(hiz / 0.75);
    //   m_talon_rearRight.set(ControlMode.PercentOutput, hiz / 0.75);
    // }

    // Kendi ekseninde donme
    if (((donushizi < -sinir) || (donushizi > sinir)) && ((hiz < sinir) && (hiz > -sinir))) {
      // m_frontLeftMotor.set(-donushizi);
      m_talon_rearLeft.set(ControlMode.PercentOutput, donushizi);
      m_talon_frontLeft.set(ControlMode.PercentOutput, donushizi);
      m_talon_rearRight.set(ControlMode.PercentOutput, -donushizi);
      m_talon_frontRight.set(ControlMode.PercentOutput, -donushizi);

    }

    // // Sağ Gitme
    // if (((hiz > sinir) || (hiz < -sinir)) && (donushizi > sinir)) {
    //   m_frontLeftMotor.set(hiz);
    //   m_talon_rearLeft.set(ControlMode.PercentOutput, -hiz);
    //   m_frontRightMotor.set(hiz * donushizi);
    //   m_talon_rearRight.set(ControlMode.PercentOutput, -hiz * donushizi);
    // }

    // // Sol Gitme
    // if (((hiz > sinir) || (hiz < -sinir)) && (donushizi < -sinir)){
    //   m_frontLeftMotor.set(hiz * -donushizi);
    //   m_talon_rearLeft.set(ControlMode.PercentOutput, -hiz * -donushizi);
    //   m_frontRightMotor.set(hiz);
    //   m_talon_rearRight.set(ControlMode.PercentOutput, -hiz); 
    // }

    // Duz gitme
    if (((hiz > sinir) || (hiz < -sinir)) && ((donushizi < sinir) && (donushizi > -sinir))) {
      m_talon_frontLeft.set(ControlMode.PercentOutput, -hiz);
      m_talon_rearLeft.set(ControlMode.PercentOutput, -hiz);
      m_talon_frontRight.set(ControlMode.PercentOutput, -hiz);
      m_talon_rearRight.set(ControlMode.PercentOutput, -hiz);
    
    }
    // // Sağ-Sol Gitmeme
    // if (((donushizi > sinir) || (donushizi < -sinir)) && ((hiz > -sinir) && (hiz
    // < sinir)))
    // m_frontLeftMotor.set(hiz * 0);
    // m_rearLeftMotor.set(hiz * 0);
    // m_frontRightMotor.set(hiz * 0);
    // m_rearRightMotor.set(hiz * 0);

    // Durma
    // if (((hiz < sinir) && (hiz > -sinir)) && ((donushizi < sinir) && (donushizi >
    // -sinir)) && ((mevlana < sinir) && (mevlana > -sinir))) {
    // // Changes might be required here...
    // Double slow = Math.pow(m_leftStick.getRawAxis(1), 3);
    // // System.out.println(slow);
    // m_frontLeftMotor.set(slow);
    // m_rearLeftMotor.set(slow);
    // m_frontRightMotor.set(slow);
    // m_rearRightMotor.set(slow);
    // }
    if ((((hiz < sinir) && (hiz > -sinir)) && ((donushizi < sinir) && (donushizi > -sinir))
        && ((mevlana < sinir) && (mevlana > -sinir)))) {
      m_talon_frontLeft.set(ControlMode.PercentOutput, 0);
      m_talon_rearLeft.set(ControlMode.PercentOutput, 0);
      m_talon_frontRight.set(ControlMode.PercentOutput, 0);
      m_talon_rearRight.set(ControlMode.PercentOutput, 0);

    }
  }

  // int sayac = 0;
  // boolean ilkmi2 = true;

  private void Autonomous() {

  //   sayac++;
  //   if (ilkmi2) {
  //     // comp.disable();
  //     comp.enableDigital();
  //     solenoid.set(DoubleSolenoid.Value.kForward);
  //     ilkmi2 = false;
  //   }

  //   if (sayac > 150 && sayac < 200) {
  //     m_frontLeftMotor.set(0.5);
  //     m_talon_rearLeft.set(ControlMode.PercentOutput, 0.5);
  //     m_frontRightMotor.set(-0.5);
  //     m_talon_rearRight.set(ControlMode.PercentOutput, -0.5);
  //   }

  //   if (sayac > 250 && sayac < 500) {
  //     // mytalon.set(ControlMode.PercentOutput, -1);
  //   }

  //   if (sayac > 500 && sayac < 505) {
  //     comp.disable();
  //     solenoid.set(DoubleSolenoid.Value.kReverse);
  //   }

  //   if (sayac > 550 && sayac < 620) {
  //     // mytalon.set(ControlMode.PercentOutput, 0);
  //     m_frontLeftMotor.set(0.5);
  //     m_talon_rearLeft.set(ControlMode.PercentOutput, 0.5);
  //     m_frontRightMotor.set(-0.5);
  //     m_talon_rearRight.set(ControlMode.PercentOutput, -0.5);
  //   }

  }
}
