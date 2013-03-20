package com.hehe;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;

import javax.media.ConfigureCompleteEvent;
import javax.media.Control;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Owned;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.Renderer;
import javax.media.ResourceUnavailableEvent;
import javax.media.SizeChangeEvent;
import javax.media.Time;
import javax.media.control.BufferControl;
import javax.media.protocol.DataSource;

/**
 * Sample program to playback the input URL and set the audio capture and
 * rendering buffer sizes.
 */
public class AudioBufferControl extends Frame implements ControllerListener {
	Processor p;
	Object waitSync = new Object();
	boolean stateTransitionOK = true;

	/**
	 * Given a DataSource, create a processor and use that processor as a player
	 * to playback the media.
	 */
	public boolean open(DataSource ds, int captureBufSize, int renderBufSize) {
		System.err.println("create processor for: " + ds.getContentType());
		// Check to see if there's a buffer control on the data source.
		// It could be that we are using a capture data source.
		Control c = (Control) ds
				.getControl("javax.media.control.BufferControl");
		if (c != null)
			((BufferControl) c).setBufferLength(captureBufSize);
		try {
			p = Manager.createProcessor(ds);
		} catch (Exception e) {
			System.err
					.println("Failed to create a processor from the given DataSource: "
							+ e);
			return false;
		}
		p.addControllerListener(this);
		// Put the Processor into configured state.
		p.configure();
		if (!waitForState(p.Configured)) {
			System.err.println("Failed to configure the processor.");
			return false;
		}
		// So I can use it as a player.
		p.setContentDescriptor(null);
		p.realize();
		if (!waitForState(p.Realized)) {
			System.err.println("Failed to realize the processor.");
			return false;
		}
		// After the processor has been realized, we can now set the
		// renderer's buffer size. We need to do this before the
		// processor is prefetched.
		// We need to loop the array of controls to make sure that we
		// are setting the size of the correct buffer control since
		// the DataSource's controls are also included in the list.
		Control cs[] = p.getControls();
		Object owner;
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] instanceof Owned && cs[i] instanceof BufferControl) {
				owner = ((Owned) cs[i]).getOwner();
				if (owner instanceof Renderer) {
					((BufferControl) cs[i]).setBufferLength(renderBufSize);
				}
			}
		}
		// Prefetch the processor.
		p.prefetch();
		if (!waitForState(p.Prefetched)) {
			System.err.println("Failed to prefetch the processor.");
			return false;
		}
		// Display the visual & control component if there's one.
		setLayout(new BorderLayout());
		Component cc;
		Component vc;
		if ((vc = p.getVisualComponent()) != null) {
			add("Center", vc);
		}
		if ((cc = p.getControlPanelComponent()) != null) {
			add("South", cc);
		}
		// Start the processor.
		p.start();
		setVisible(true);
		return true;
	}

	public void addNotify() {
		super.addNotify();
		pack();
	}

	/**
	 * Block until the processor has transitioned to the given state. Return
	 * false if the transition failed.
	 */
	boolean waitForState(int state) {
		synchronized (waitSync) {
			try {
				while (p.getState() < state && stateTransitionOK)
					waitSync.wait();
			} catch (Exception e) {
			}
		}
		return stateTransitionOK;
	}

	/**
	 * Controller Listener.
	 */
	public void controllerUpdate(ControllerEvent evt) {
		if (evt instanceof ConfigureCompleteEvent
				|| evt instanceof RealizeCompleteEvent
				|| evt instanceof PrefetchCompleteEvent) {
			synchronized (waitSync) {
				stateTransitionOK = true;
				waitSync.notifyAll();
			}
		} else if (evt instanceof ResourceUnavailableEvent) {
			synchronized (waitSync) {
				stateTransitionOK = false;
				waitSync.notifyAll();
			}
		} else if (evt instanceof EndOfMediaEvent) {
			p.setMediaTime(new Time(0));
			// p.start();
			// p.close();
			// System.exit(0);
		} else if (evt instanceof SizeChangeEvent) {
		}
	}

	// Default buffer size in milli-seconds.
	static final int DEF_CAPTURE_SIZE = 62;
	static final int DEF_RENDER_SIZE = 400;

	/**
	 * Main program
	 */
	public static void main(String[] args) {
		String mlStr = null;
		int captureBufSize = DEF_CAPTURE_SIZE;
		int renderBufSize = DEF_RENDER_SIZE;
		int i = 0;
		while (i < args.length) {
			if (args[i].equals("-c")) {
				i++;
				if (i >= args.length) {
					prUsage();
					System.exit(0);
				}
				try {
					captureBufSize = Integer.parseInt(args[i]);
				} catch (NumberFormatException e) {
					prUsage();
					System.exit(0);
				}
			} else if (args[i].equals("-r")) {
				i++;
				if (i >= args.length) {
					prUsage();
					System.exit(0);
				}
				try {
					renderBufSize = Integer.parseInt(args[i]);
				} catch (NumberFormatException e) {
					prUsage();
					System.exit(0);
				}
			} else {
				mlStr = args[i];
			}
			i++;
		}
		if (mlStr == null) {
			prUsage();
			System.exit(0);
		}
		MediaLocator ml;
		if ((ml = new MediaLocator(mlStr)) == null) {
			System.err.println("Cannot build media locator from: " + mlStr);
			prUsage();
			System.exit(0);
		}
		DataSource ds = null;
		// Create a DataSource given the media locator.
		try {
			ds = Manager.createDataSource(ml);
		} catch (Exception e) {
			System.err.println("Cannot create DataSource from: " + ml);
			System.exit(0);
		}
		AudioBufferControl abc = new AudioBufferControl();
		if (!abc.open(ds, captureBufSize, renderBufSize))
			System.exit(0);
	}

	static void prUsage() {
		System.err.println("Usage: java AudioBufferControl [-c <capture buf size in millisecs>] [-r <render buf size inmillisecs>] <url>");
	}
}