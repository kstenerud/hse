MODULE BASE


======
NOTES:
======

This is the HSE base module, providing the base framework for applications.



==============
CONFIGURATION:
==============

The following properties must be present in application.properties when
using this module:

/===========================================================================\

# ---------------------------------------------------------------------------
# Base Properties
# ---------------------------------------------------------------------------

# Name of the bean implementing org.springframework.context.MessageSource
bean.messageSource=myMessageSource

# Name of the bean extending org.stenerud.hse.base.ui.echo2.Echo2UserInterface
bean.userInterface=myUserInterface


# Location where the user will be redirected when their session ends.
# If this property is not defined, the UI will show the initial screen when a
# session "ends".
endSession.redirect=myUrl

\===========================================================================/



========
SCREENS:
========

The following screens are available:
 
  mainScreen: A simple screen that prompts the user to select another screen.
