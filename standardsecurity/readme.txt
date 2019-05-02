MODULE STANDARDSECURITY


======
NOTES:
======

This module provides a simple security and login system, suitable for most
small to medium sized applications.

Authentication is password-based, and the password is stored in the User table
for simplicity.
A group-based permission system is included, and allows for a specific "level"
of a permission to be granted to a Group (e.g. 0=none, 1=read, 2=write).
Users gain permissions through membership to a Group.  A User can be a member
of only one Group.

Also included are editor screens for users and groups.

The initializer for standardsecurity will attempt to create an administrator
group "Administrator" if none is present.  As well, it will attempt to make
an administrator user if none is present.

The administrator group and user cannot be deleted.  As well, the administrator
group will always have permission to modify groups, even if "none" is selected
in the group permission editor.  This prevents the application from getting
into a state where nobody has any administrative privileges.

Have a look at org.stenerud.hse.standardsecurity.security.Permissions and the
business objects under org.stenerud.hse.standardsecurity.business to get an
idea of how to use the permission system.


==============
CONFIGURATION:
==============

The following properties may be specified in application.properties when
using this module:

/===========================================================================\

# ---------------------------------------------------------------------------
# Standard Security Properties
# ---------------------------------------------------------------------------

# Allow updating of the administrator group (default true)
administrator.group.allowUpdate=true

# Allow updating of the administrator user (default true)
administrator.user.allowUpdate=true

# Default name to use when first creating the administrator user (default admin)
administrator.defaultName=admin

# Default name to use when first creating the administrator password (default adminchangeme)
administrator.defaultPassword=adminchangeme

\===========================================================================/


Services provided:

DAOs:
groupDAO
securityDAO
userDAO

Business:
groupBusiness
userBusiness

Services:
security
permissions
currentUser



========
SCREENS:
========

The following screens are available for your screen.properties file:

  loginScreen:       A username/password login screen.
  groupEditorScreen: A CRUD group editor.
  userEditorScreen:  A CRUD user editor.
