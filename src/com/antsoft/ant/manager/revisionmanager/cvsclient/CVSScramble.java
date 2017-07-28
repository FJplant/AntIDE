/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/revisionmanager/cvsclient/CVSScramble.java,v 1.3 1999/07/22 03:02:45 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: CVSScramble.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:51p
 * Updated in $/AntIDE/source/ant/manager/revisionmanager/cvsclient
 * 
 * *****************  Version 6  *****************
 * User: Flood        Date: 98-09-18   Time: 1:59p
 * Updated in $/JavaProjects/src/ant/manager/revisionmanager/cvsclient
 * 
 * *****************  Version 5  *****************
 * User: Multipia     Date: 98-09-09   Time: 2:33p
 * Updated in $/Ant/src/ant/revisionmanager
 * package name changed to
 * package ant.revisionmanager;
 * 
 * *****************  Version 3  *****************
 * User: Flood        Date: 98-07-28   Time: 4:33a
 * Updated in $/Ant/src
 * 
 * *****************  Version 2  *****************
 * User: Flood        Date: 98-07-28   Time: 2:21a
 * Updated in $/Ant/src
 */

 package ant.manager.revisionmanager.cvsclient;

 import java.security.InvalidParameterException;

/**
 * @author Ç¥ÅÂ¼ö
 * password scrambler ( from cvs-1.9.22/src/scramble.c )
 *   ------ comment from cvs-1.9.22/src/scramble.c ---------------------
 *   Trivially encode strings to protect them from innocent eyes (i.e.,
 *   inadvertent password compromises, like a network administrator
 *   who's watching packets for legitimate reasons and accidentally sees
 *   the password protocol go by).
 *
 *   This is NOT secure encryption.
 *
 *   It would be tempting to encode the password according to username
 *   and repository, so that the same password would encode to a
 *   different string when used with different usernames and/or
 *   repositories.  However, then users would not be able to cut and
 *   paste passwords around.  They're not supposed to anyway, but we all
 *   know they will, and there's no reason to make it harder for them if
 *   we're not trying to provide real security anyway.
 *   -------------------------------------------------------------------
 */

 public class CVSScramble extends Object
 {
   static int shifts[] = {
     0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
    16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
   114,120, 53, 79, 96,109, 72,108, 70, 64, 76, 67,116, 74, 68, 87,
   111, 52, 75,119, 49, 34, 82, 81, 95, 65,112, 86,118,110,122,105,
    41, 57, 83, 43, 46,102, 40, 89, 38,103, 45, 50, 42,123, 91, 35,
   125, 55, 54, 66,124,126, 59, 47, 92, 71,115, 78, 88,107,106, 56,
    36,121,117,104,101,100, 69, 73, 99, 63, 94, 93, 39, 37, 61, 48,
    58,113, 32, 90, 44, 98, 60, 51, 33, 97, 62, 77, 84, 80, 85,223,
   225,216,187,166,229,189,222,188,141,249,148,200,184,136,248,190,
   199,170,181,204,138,232,218,183,255,234,220,247,213,203,226,193,
   174,172,228,252,217,201,131,230,197,211,145,238,161,179,160,212,
   207,221,254,173,202,146,224,151,140,196,205,130,135,133,143,246,
   192,159,244,239,185,168,215,144,139,165,180,157,147,186,214,176,
   227,231,219,169,175,156,206,198,129,164,150,210,154,177,134,127,
   182,128,158,208,162,132,167,209,149,241,153,251,237,236,171,195,
   243,233,253,240,194,250,191,155,142,137,245,235,163,242,178,152 };

    /**
     * scamble the password
     * @param password string to be scrambled
     * @return scrambled string
     */
		 public static String scramble(String passwd)
	   {
      StringBuffer scrambled = new StringBuffer("A");
      /* the 'A' prefix that indicates which version of
         scrambling this is (the first, obviously, since we only do one
         kind of scrambling so far) */

      for (int i = 0; i < passwd.length() ; i++)
	       scrambled.append((char)shifts[(int)(passwd.charAt(i))]);

      return scrambled.toString();
     }

    /**
     * descamble the password
     * @param scrambled scrambled string
     * @return descrambled string
     */
     public static String descramble(String scrambled) throws InvalidParameterException
     {
      /* For now we can only handle one kind of scrambling.  In the future
         there may be other kinds, and this `if' will become a `switch'.  */
      if (scrambled.charAt(0) != 'A')
      	throw new InvalidParameterException("can handle only 'A' prefix");

      /* Method `A' is symmetrical, so scramble again to decrypt. */
      return scramble(scrambled.substring(1)).substring(1);
     }

 }

