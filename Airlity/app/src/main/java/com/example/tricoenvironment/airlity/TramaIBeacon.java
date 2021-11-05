/**
 * TramaIBeacon.java
 * @fecha: 07/10/2021
 * @autor: Jordi Bataller i Mascarell
 *
 * @Descripcion:
 * Este fichero contiene la clase TramaIBeacon que desgrana la información en bytes de los
 * beacons recibidos
 */

package com.example.tricoenvironment.airlity;
import java.util.Arrays;

public class TramaIBeacon {
    private byte[] prefijo = null; // 9 bytes
    private byte[] uuid = null; // 16 bytes
    private byte[] temperatura = null; // 2 bytes
    private byte[] humedad = null; // 2 bytes

    private byte[] concentracion = null; // 2 bytes
    private byte txPower = 0; // 1 byte

    private byte[] losBytes;

    private byte[] advFlags = null; // 3 bytes
    private byte[] advHeader = null; // 2 bytes
    private byte[] companyID = new byte[2]; // 2 bytes
    private byte iBeaconType = 0 ; // 1 byte
    private byte iBeaconLength = 0 ; // 1 byte

    // -------------------------------------------------------------------------------
    // [byte] <- getPrefijo() <-
    // -------------------------------------------------------------------------------
    public byte[] getPrefijo() {
        return prefijo;
    }

    // -------------------------------------------------------------------------------
    // [byte] <- getUUID() <-
    // -------------------------------------------------------------------------------
    public byte[] getUUID() {
        return uuid;
    }

    // -------------------------------------------------------------------------------
    // [byte] <- getMajor() <-
    // -------------------------------------------------------------------------------
    public byte[] getTemperatura() {
        return temperatura;
    }

    // -------------------------------------------------------------------------------
    // [byte] <- getMajor() <-
    // -------------------------------------------------------------------------------
    public byte[] getHumedad() {
        return humedad;
    }

    // -------------------------------------------------------------------------------
    // [byte] <- getMinor() <-
    // -------------------------------------------------------------------------------
    public byte[] getConcentracion() {
        return concentracion;
    }

    // -------------------------------------------------------------------------------
    // byte <- getTxPower() <-
    // -------------------------------------------------------------------------------
    public byte getTxPower() {
        return txPower;
    }

    // -------------------------------------------------------------------------------
    // [byte] <- getLosBytes() <-
    // -------------------------------------------------------------------------------
    public byte[] getLosBytes() {
        return losBytes;
    }

    // -------------------------------------------------------------------------------
    // [byte] <- getAdvFlags() <-
    // -------------------------------------------------------------------------------
    public byte[] getAdvFlags() {
        return advFlags;
    }

    // -------------------------------------------------------------------------------
    // [byte] <- getAdvHeader() <-
    // -------------------------------------------------------------------------------
    public byte[] getAdvHeader() {
        return advHeader;
    }

    // -------------------------------------------------------------------------------
    // [byte] <- getCompanyID() <-
    // -------------------------------------------------------------------------------
    public byte[] getCompanyID() {
        return companyID;
    }

    // -------------------------------------------------------------------------------
    // byte <- getIBeaconType() <-
    // -------------------------------------------------------------------------------
    public byte getiBeaconType() {
        return iBeaconType;
    }

    // -------------------------------------------------------------------------------
    // byte <- getiBeaconLength() <-
    // -------------------------------------------------------------------------------
    public byte getiBeaconLength() {
        return iBeaconLength;
    }

    // -------------------------------------------------------------------------------
    // [byte] -> Constructor() ->
    // -------------------------------------------------------------------------------
    public TramaIBeacon(byte[] bytes ) {
        this.losBytes = bytes;

        prefijo = Arrays.copyOfRange(losBytes, 0, 8+1 ); // 9 bytes
        uuid = Arrays.copyOfRange(losBytes, 9, 24+1 ); // 16 bytes
        concentracion = Arrays.copyOfRange(losBytes, 25, 26+1 ); // 2 bytes
        temperatura = Arrays.copyOfRange(losBytes, 27, 27+1 ); // 1bytes
        humedad = Arrays.copyOfRange(losBytes, 28, 28+1 ); // 1 bytes
        txPower = losBytes[ 29 ]; // 1 byte

        advFlags = Arrays.copyOfRange( prefijo, 0, 2+1 ); // 3 bytes
        advHeader = Arrays.copyOfRange( prefijo, 3, 4+1 ); // 2 bytes
        companyID = Arrays.copyOfRange( prefijo, 5, 6+1 ); // 2 bytes
        iBeaconType = prefijo[ 7 ]; // 1 byte
        iBeaconLength = prefijo[ 8 ]; // 1 byte

    } // ()
} // class
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------


