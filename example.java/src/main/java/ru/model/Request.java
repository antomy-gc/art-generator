package ru.model;

import io.art.server.validation.*;
import lombok.*;
import static io.art.server.validation.Validators.*;
import java.util.*;

@Value
@Builder
public class Request {
    int FInteger;
    char FCharacter;
    short FShort;
    byte FByte;
    boolean FBoolean;
    long FLong;
    double FDouble;
    float FFloat;

    Integer FBInteger;
    Short FBShort;
    Byte FBByte;
    Boolean FBBoolean;
    Long FBLong;
    String FBString;
    Double FBDouble;
    Float FBFloat;

    int[] FAint;
    char[] FAchar;
    short[] FAshort;
    byte[] FAbyte;
    boolean[] FAboolean;
    long[] FAlong;
    double[] FAdouble;
    float[] FAfloat;

    Integer[] FABInteger;
    Character[] FABCharacter;
    Short[] FABShort;
    Byte[] FABByte;
    Boolean[] FABBoolean;
    Long[] FABLong;
    String[] FABString;
    Double[] FABDouble;
    Float[] FABFloat;

    Collection<Integer> FCInteger;
    Collection<Character> FCCharacter;
    Collection<Short> FCShort;
    Collection<Byte> FCByte;
    Collection<Boolean> FCBoolean;
    Collection<Long> FCLong;
    Collection<String> FCString;
    Collection<Double> FCDouble;
    Collection<Float> FCFloat;

    List<Integer> FLInteger;
    List<Character> FLCharacter;
    List<Short> FLShort;
    List<Byte> FLByte;
    List<Boolean> FLBoolean;
    List<Long> FLLong;
    List<String> FLString;
    List<Double> FLDouble;
    List<Float> FLFloat;

    Set<Integer> FSInteger;
    Set<Character> FSCharacter;
    Set<Short> FSShort;
    Set<Byte> FSByte;
    Set<Boolean> FSBoolean;
    Set<Long> FSLong;
    Set<String> FSString;
    Set<Double> FSDouble;
    Set<Float> FSFloat;

    Map<Integer, Integer> FMInteger;
    Map<Character, Character> FMCharacter;
    Map<Short, Short> FMShort;
    Map<Byte, Byte> FMByte;
    Map<Boolean, Boolean> FMBoolean;
    Map<Long, Long> FMLong;
    Map<String, String> FMString;
    Map<Double, Double> FMDouble;
    Map<Float, Float> FMFloat;

    Model FModel;

    Model[] FAModel;
    List<Model>[] FALModel;
    Set<Model>[] FASModel;
    Collection<Model>[] FACModel;
    Map<String, Model>[] FAMModel;

    List<Model> FLModel;
    List<List<Model>> FLLModel;
    List<Set<Model>> FLSModel;
    List<Collection<Model>> FLCModel;
    List<Map<String, Model>> FLMModel;
    List<Model[]> FLAModel;

    Set<Model> FSModel;
    Set<List<Model>> FSLModel;
    Set<Set<Model>> FSSModel;
    Set<Collection<Model>> FSCModel;
    Set<Map<String, Model>> FSMModel;
    Set<Model[]> FSAModel;

    Collection<Model> FCModel;
    Collection<List<Model>> FCLModel;
    Collection<Set<Model>> FCSModel;
    Collection<Collection<Model>> FCCModel;
    Collection<Map<String, Model>> FCMModel;
    Collection<Model[]> FCAModel;

    Map<String, Model> FMModel;
    Map<String, Model[]> FMAModel;
    Map<String, List<Model>> FMLModel;
    Map<String, Set<Model>> FMSModel;
    Map<String, Collection<Model>> FMCModel;
    Map<String, Map<String, Model>> FMMModel;

    Collection<List<Map<String, Model>>> FCCMModel;
    Collection<Set<Map<String, Model>>> FCLMModel;
    Collection<Collection<Map<String, Model>>> FCSMModel;
    Collection<Map<String, Model>> FCMMModel;

    Collection<List<Map<String, Model>>[]> FCLMAModel;
    Collection<Set<Map<String, Model>>[]> FCSMAModel;
    Collection<Collection<Map<String, Model>>[]> FCCMAModel;
    Collection<Map<String, Model>[]> FCMAModel;
}
