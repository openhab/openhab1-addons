/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Moka7;

// See §33.19 of "System Software for S7-300/400 System and Standard Functions"
public class S7Protection {
   public int sch_schal;
   public int sch_par;
   public int sch_rel;
   public int bart_sch;
   public int anl_sch;
   protected void Update(byte[] Src)
   {
       sch_schal = S7.GetWordAt(Src,2);
       sch_par   = S7.GetWordAt(Src,4);
       sch_rel   = S7.GetWordAt(Src,6);
       bart_sch  = S7.GetWordAt(Src,8);
       anl_sch   = S7.GetWordAt(Src,10);
   }
}
