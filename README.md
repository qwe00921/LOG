LOG
===

Android Log wrapper. Make your lovely cat simpler

##What new?

Nothing new, but you can save your time finding where the logs were printed and put `tag` into `Log.blah(tag, msg)`

Example:
Your log is writen in `Foo.java`, at line 145:

  Foo:145 : your message here



##Usage

Download and put LOG.java to your application

  LOG log = new LOG();
  log.i("your message or your array or your list here");
  log.e("something like above, but you can throw an exception", exception);
  log.wtf(); //just WHAT THE FUCK!!!!!!!!!! <-- I know what your feeling LOL
  log.here(); //just here <-- I know something, you just want this 
  
##License:

Copyright: tuanchauict

  
