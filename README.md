This program allows for the creation of fractals with multiple variables.

The settings panel can be used to edit variables.
Add, delete, and select a variable with the buttons and drop down menu.

The first two text boxes sets the default value of each variable.
 - variable 0's default value is automatically set to a pixel's location on the complex plane and cannot be changed.
 - values are saved and evaluated on enter. Clicking off without pressing enter will not save any changes.
The thrid text box sets a variable's equation.
 - an equation can be set for variable 0.
 - variable without an equation will remain constant.
 - Equation formatting:
     - supported operations: +,-,*,/, ^
     - functions: sin, cos, tan, len, len2, sqrt
  
     - variables are represented as vn, with n being the variable number. v0 = variable 0, v1 = variable 1,... vn = variable n
     - constants can be declared using c(r,i), c(r), or c(i).  c(5,6i) = 5+6i, c(-7,-8i) = -7-8i 
     - functions are declared using F, followed by the function name. Fsin(c(5,6i) = sin(5+6i), Fsqrt(Flen2(c(7,-8i))) = Flen(c(-7,8i)) = |-7-8i|
     - multiplication can be done without *   c(5)c(6i) = c(5)*c(6i)
     - equation interpreter follows pemdas.
  
     - Equations are evaluated on enter press. Clicking off without pressing enter will not save any changes.

  - Fractal coloring:
      - currently it is only based on how many iterations it took for Flen2(v1) to reach 40000!
      - fields for setting custom equations to determin the RGB value of each pixel will be added.
        
  - Equation examples:
        - Mandelbrot set:
              - v0: empty
              - v1: default:0,0i equation: v1^c(2)+v0
        - Julia set:
              - v0: equation: v0^c(2)+v2
              - v1: default: 0,0i equation: v0
              - v2: default: 0.3, 0.6i

  - Rendering:
        - render using the main and preview buttons.
        - main will render the fractal at full resolution (currently hard coded to 800x800)
        - preview renders a low resolution image of the visible and surrounding aera.

  - Move by dragging the mouse and mouse wheel, or wasd.
