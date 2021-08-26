//
//  MIDS.swift
//  ConnectMe
//
//  Created by evernym on 28/07/21.
//  Copyright © 2021 Facebook. All rights reserved.
//

import Foundation
import MIDSAssistSDK

@objc(MIDSDocumentVerification)
class MIDSDocumentVerification: NSObject {
  static var enrollmentManagerInstance: MIDSEnrollmentManager!
  static var enrollmentManager: MIDSEnrollmentManager = getEnrollmentManagerInstance()
  var resolve: RCTResponseSenderBlock!
  var reject: RCTResponseSenderBlock!
  
  static func getEnrollmentManagerInstance() -> MIDSEnrollmentManager {
      if enrollmentManagerInstance == nil {
          enrollmentManagerInstance = MIDSEnrollmentManager.shared()
      }
      return enrollmentManagerInstance
  }
  
  @objc func initMIDSSDK(_ token: String,
                         withDataCenter dataCenter: String,
                         resolver resolve: @escaping RCTResponseSenderBlock,
                         rejecter reject: @escaping RCTResponseSenderBlock) -> Void {
      DispatchQueue.main.async {
        MIDSDocumentVerification.enrollmentManager.enrollmentDelegate = self
        let dataCenter = self.getDataCenter(dataCenter: dataCenter)
        self.resolve = resolve
        self.reject = reject
        MIDSDocumentVerification.enrollmentManager.initializeMIDSVerifySDK(sdkToken: token, dataCenter: dataCenter)
      }
  }
  
  @objc func getCountryList(_ resolve: @escaping RCTResponseSenderBlock,
                            rejecter reject: @escaping RCTResponseSenderBlock) -> Void {
    let countryList = MIDSDocumentVerification.enrollmentManager.getCountryList()
    var countries = [String: String]()
    for country in countryList {
      if let countryName = country.countryName, let countryCode = country.countryCode {
        countries[countryName] = countryCode
      }
    }

    resolve([countries])
  }
  
  @objc func getDocumentTypes(_ countryCode: String,
                              resolver resolve: @escaping RCTResponseSenderBlock,
                              rejecter reject: @escaping RCTResponseSenderBlock) -> Void {
    let documentType = MIDSDocumentVerification.enrollmentManager.getDocumentTypes(countryCode: countryCode)
    resolve([documentType])
  }
  
  @objc func startMIDSSDKScan(_ documentType: String,
                              policyVersion version: String,
                              resolver resolve: @escaping RCTResponseSenderBlock,
                              rejecter reject: @escaping RCTResponseSenderBlock) -> Void {
    DispatchQueue.main.async {
      MIDSDocumentVerification.enrollmentManager.startScan(document: documentType, privacyPolicyVersion: version, userBiometricConsent: true)
    }
    self.resolve = resolve
    self.reject = reject
  }

  func getDataCenter(dataCenter: String) -> MIDSDataCenter {
    switch dataCenter {
    case "SG":
      return .MIDSDataCenterSG;
    case "US":
      return .MIDSDataCenterUS;
    case "EU":
      return .MIDSDataCenterEU
    default:
      return .MIDSDataCenterSG;
    }
  }
  
  func handleMIDSError(error: MIDSVerifyError) {
    if self.reject != nil {
      self.reject([])
      self.reject = nil
    }
    switch error.errorCode {
    case 2002:
        return
    case 2003, 2010:
        MIDSDocumentVerification.enrollmentManager.terminateSDK()
    default:
        MIDSDocumentVerification.enrollmentManager.terminateSDK()
    }
  }
}

extension MIDSDocumentVerification: MIDSEnrollmentDelegate {
  func midsEnrollmentManager(scanViewController: MIDSCustomScanViewController, shouldDisplayNoUSAddressFoundHint message: String, confirmation: @escaping () -> Void) {
      NSLog("no US address")
  }

  func midsEnrollmentManager(didFinishInitializationSuccess status: Bool) {
    if self.resolve != nil {
      self.resolve([])
      self.resolve = nil
    }
    
//    MIDSDocumentVerification.enrollmentManager.enrollmentDelegate = self
  }
  
  func midsEnrollmentManager(didFinishInitializationWithError error: MIDSVerifyError) {
      handleMIDSError(error: error)
  }
  
  func midsEnrollmentManager(didDetermineNextScanViewController scanViewController: MIDSCustomScanViewController, isFallback: Bool) {
    if  scanViewController.customScanViewController?.currentScanMode() == .faceCapture || scanViewController.customScanViewController?.currentScanMode() == .faceIProov {
      UIApplication.shared.windows.first?.rootViewController?.dismiss(animated: true, completion:{ () -> Void in
        UIApplication.shared.windows.first?.rootViewController?.present(scanViewController, animated: true)
      })
      return
    }

    if (UIApplication.shared.windows.first?.rootViewController?.isKind(of: MIDSCustomScanViewController.self) ?? false) {
      UIApplication.shared.windows.first?.rootViewController?.dismiss(animated: false)
    }
    UIApplication.shared.windows.first?.rootViewController?.present(scanViewController, animated: false)
  }

  func midsEnrollmentManager(didFinishScanningWith reference: String, accountID: String?, authenticationResult: Bool?)  {
    MIDSDocumentVerification.enrollmentManager.terminateSDK()
    if self.resolve != nil {
      self.resolve([reference])
      self.resolve = nil
      UIApplication.shared.windows.first?.rootViewController?.dismiss(animated: true)
    }
  }

  func midsEnrollmentManager(didCancelWithError error: MIDSVerifyError) {
    handleMIDSError(error: error)
  }

  func midsEnrollmentManager(scanViewController: MIDSCustomScanViewController, shouldDisplayHelpWithText message: String, animationView: UIView) {
    scanViewController.customScanViewController?.retryScan()
  }
  
  func midsEnrollmentManager(shouldDisplayConfirmationWith view: UIView, text: String, currentStep: Int, totalSteps: Int, retryEnabled: Bool, confirmEnabled: Bool, confirmation: (() -> Void)?, retake: (() -> Void)?) {
    guard let _ = confirmation else { return }
    let _confirmationAction = confirmation
    _confirmationAction!()
  }
  
  func midsEnrollmentManager(didStartBiometricAnalysis scanViewController: MIDSCustomScanViewController) {}
  
  func midsEnrollmentManager(customScanViewControllerWillPresentIProovController scanViewController: MIDSCustomScanViewController) {}
  
  func midsEnrollmentManager(customScanViewControllerWillPrepareIProovController scanViewController: MIDSCustomScanViewController) {}
  
  func midsEnrollmentManager(didCaptureAllParts status: Bool) {}
}
