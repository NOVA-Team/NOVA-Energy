###Author:
[ExE Boss](https://github.com/ExE-Boss)

### About
This file contains my docs of mod energy conversion.  
It was created by observing what ratios other mods used when implementing energy conversion.

# Recalculated Energy Conversions to remove infinite power generation loops
- Defined by Player:
    - 1 EU = 1 FZ-Charge 
    - 3 EU = 25 RF
- Defined by Eloraam:
    - 1 kW (RP2) = 1 McJ
- Defined by Thermal Expansion:
	- 1 McJ = 25 RF

## Derived conversions:
- 3 EU = 1 McJ
- 3 EU = 1 kW (RP2)
- 1 EU = 40 J (Defined by AE)
- 1 McJ = 120 J (Prevents infinite loop)

---

Original file:

# Mod Energy Conversion by Unit
- 1 McJ = 3 EU (Probably from Forestry)
- 1 EU = 2 Unit
- 1 McJ = 5 Unit (<^ These lead to an infinite power generation loop, should redefine McJ:Unit to 1:6)
- 1 EU = 45 J (Average of AE and MMMPS)
- 1 McJ = 60 J (Leads to infinite loop)
- 1 kW (RP2) = 1 McJ (As defined by Eloraam)
- 1 kW (RP2) = ~3 EU
- 1 kW (RP2) = 60 J
- 1 kW (RP2) = 5-ish Units
- 3 EU = 25 RF
- 1 EU = 1 FZ-Charge
- 1 McJ = 25 RF

## Math behind some of the conversions
- 1 McJ = 3 EU = 1 kW (RP2)  
- ~3 EU = 1 kW (RP2)  
- 1 McJ = 5 Units = 2.5 EU  
- 1 EU = 2 Units  
- 20 J = 1 Unit = 0.5 EU  
- 40 J = 1 EU (AE)  
- 50 J = 1 EU (MMMPS)  
- 60 J = 1 McJ = 1 kW (RP2)  

---

# Licence
This work by [ExE Boss](https://github.com/ExE-Boss) is dual-licensed
under a [Creative Commons Attribution-ShareAlike 4.0 International License](https://creativecommons.org/licenses/by-sa/4.0/)
and a [GNU Lesser General Public License (LGPL) version 3](https://www.gnu.org/licenses/lgpl-3.0.html).
