<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>新富文本消息 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/rich-push.css" media="screen">
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/sidebar.jsp"%>

				<div class="right_content">
					<ul id="rich-push-steps">
						<li style="z-index: 6;"><a href="#" id="step-nav-0"
							class="active">Get Started</a>
						</li>
						<li style="z-index: 5;"><a href="#" id="step-nav-1">Message</a>
						</li>
						<li style="z-index: 4;"><a href="#" id="step-nav-2">Push
								Alert</a>
						</li>
						<li style="z-index: 3;"><a href="#" id="step-nav-3">Recipients</a>
						</li>
						<li style="z-index: 2;"><a href="#" id="step-nav-4">Review</a>
						</li>
						<li style="z-index: 1;"><a href="#" id="step-nav-5">Done</a>
						</li>
					</ul>
					<div class="bare-box" id="step-container">
						<form action="" method="post" id="rich-push-form">
							<div style="display: none">
								<input type="hidden" name="csrfmiddlewaretoken"
									value="8f403e71afcf5e5c4635d058bf6a7985">
							</div>
							<div class="step" id="step-0">
								<h2>Select a style</h2>
								<div id="template-selection">
									<div id="template-1" link-data="/media/templates/plain.html"
										class="template selected">
										<span>Plain</span>
									</div>
									<div id="template-2" link-data="/media/templates/smooth.html"
										class="template">
										<span>Smooth</span>
									</div>
									<div id="template-3" link-data="/media/templates/linen.html"
										class="template">
										<span>Linen</span>
									</div>
									<h2 style="clear: both; margin-left: -20px;">Or try one of
										our examples</h2>
									<div id="template-4"
										link-data="/media/templates/rich_push_examples/coupon.html"
										class="template">
										<span>Coupon</span>
									</div>
									<div id="template-5"
										link-data="/media/templates/rich_push_examples/nkor.html"
										class="template">
										<span>Concert Promo</span>
									</div>
									<div id="template-6"
										link-data="/media/templates/rich_push_examples/pill.html"
										class="template">
										<span>Pill Reminder</span>
									</div>
									<div id="template-7"
										link-data="/media/templates/rich_push_examples/vote.html"
										class="template">
										<span>Vote</span>
									</div>
									<div id="template-8"
										link-data="/media/templates/rich_push_examples/casestudy.html"
										class="template">
										<span>Case Study</span>
									</div>
									<input type="hidden" name="template" id="template"
										value="template-1">
								</div>
								<div class="action">
									<span class="alert"></span> <img
										src="/resources/images/circle-loader.gif" class="loader">
									<button id="rp-step1-next" rel="next">
										Start Creating Your Message <span class="next"></span>
									</button>
								</div>
							</div>
							<div class="step" id="step-1">
								<h2>Create your message</h2>
								<fieldset>
									<div class="bare-box">
										<label for="title">Give Your Message a Title <span
											class="red">*</span>
										</label> <input type="text" name="title" id="id_title" value=""
											placeholder="Enter a title">
									</div>
								</fieldset>
								<fieldset>
									<textarea name="display_message" id="display_message" rows="10"
										cols="100" class="editor" style="display: none;"
										aria-hidden="true"></textarea>
									<span role="application"
										aria-labelledby="display_message_voice"
										id="display_message_parent" class="mceEditor defaultSkin"
										style=""><span class="mceVoiceLabel"
										style="display: none;" id="display_message_voice">Rich
											Text Area</span>
									<table role="presentation" id="display_message_tbl"
											class="mceLayout" cellspacing="0" cellpadding="0"
											style="width: 663px; height: 500px;">
											<tbody>
												<tr role="presentation" class="mceFirst">
													<td class="mceToolbar mceLeft mceFirst mceLast"
														role="presentation"><div
															id="display_message_toolbargroup" role="group"
															aria-labelledby="display_message_toolbargroup_voice"
															tabindex="-1">
															<span role="application"><span
																id="display_message_toolbargroup_voice"
																class="mceVoiceLabel" style="display: none;">Toolbar</span>
															<table id="display_message_toolbar1"
																	class="mceToolbar mceToolbarRow1 Enabled"
																	cellpadding="0" cellspacing="0" align=""
																	role="presentation" tabindex="-1" aria-disabled="false"
																	aria-pressed="false">
																	<tbody>
																		<tr>
																			<td
																				class="mceToolbarStart mceToolbarStartButton mceFirst"><span>
																					<!-- IE -->
																			</span>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_bold" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_bold"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_bold_voice"
																				title="Bold (Ctrl+B)" tabindex="-1"
																				aria-pressed="false"><span
																					class="mceIcon mce_bold"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_bold_voice">Bold
																						(Ctrl+B)</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_italic" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_italic"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_italic_voice"
																				title="Italic (Ctrl+I)" tabindex="-1"
																				aria-pressed="false"><span
																					class="mceIcon mce_italic"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_italic_voice">Italic
																						(Ctrl+I)</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_underline" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_underline"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_underline_voice"
																				title="Underline (Ctrl+U)" tabindex="-1"
																				aria-pressed="false"><span
																					class="mceIcon mce_underline"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_underline_voice">Underline
																						(Ctrl+U)</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_strikethrough"
																				href="javascript:;"
																				class="mceButton mceButtonEnabled mce_strikethrough"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_strikethrough_voice"
																				title="Strikethrough" tabindex="-1"
																				aria-pressed="false"><span
																					class="mceIcon mce_strikethrough"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_strikethrough_voice">Strikethrough</span>
																			</a>
																			</td>
																			<td style="position: relative"><span
																				class="mceSeparator" role="separator"
																				aria-orientation="vertical" tabindex="-1"></span>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_justifyleft" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_justifyleft"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_justifyleft_voice"
																				title="Align left" tabindex="-1"
																				aria-pressed="false"><span
																					class="mceIcon mce_justifyleft"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_justifyleft_voice">Align
																						left</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_justifycenter"
																				href="javascript:;"
																				class="mceButton mceButtonEnabled mce_justifycenter"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_justifycenter_voice"
																				title="Align center" tabindex="-1"
																				aria-pressed="false"><span
																					class="mceIcon mce_justifycenter"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_justifycenter_voice">Align
																						center</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_justifyright"
																				href="javascript:;"
																				class="mceButton mceButtonEnabled mce_justifyright"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_justifyright_voice"
																				title="Align right" tabindex="-1"
																				aria-pressed="false"><span
																					class="mceIcon mce_justifyright"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_justifyright_voice">Align
																						right</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_justifyfull" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_justifyfull"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_justifyfull_voice"
																				title="Align full" tabindex="-1"
																				aria-pressed="false"><span
																					class="mceIcon mce_justifyfull"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_justifyfull_voice">Align
																						full</span>
																			</a>
																			</td>
																			<td class="mceToolbarEnd"><span>
																					<!-- IE -->
																			</span>
																			</td>
																			<td style="position: relative"><span
																				role="button" aria-haspopup="true"
																				aria-labelledby="display_message_fontselect_text"
																				aria-describedby="display_message_fontselect_voiceDesc"><table
																						role="presentation" tabindex="-1"
																						id="display_message_fontselect" cellpadding="0"
																						cellspacing="0"
																						class="mceListBox mceListBoxEnabled mce_fontselect"
																						aria-valuenow="Font family">
																						<tbody>
																							<tr>
																								<td class="mceFirst"><span
																									id="display_message_fontselect_voiceDesc"
																									class="voiceLabel" style="display: none;">Font
																										family</span><a id="display_message_fontselect_text"
																									tabindex="-1" href="javascript:;"
																									class="mceText mceTitle"
																									onclick="return false;"
																									onmousedown="return false;">Font family</a>
																								</td>
																								<td class="mceLast"><a
																									id="display_message_fontselect_open"
																									tabindex="-1" href="javascript:;"
																									class="mceOpen" onclick="return false;"
																									onmousedown="return false;"><span><span
																											style="display: none;" class="mceIconOnly"
																											aria-hidden="true">▼</span>
																									</span>
																								</a>
																								</td>
																							</tr>
																						</tbody>
																					</table>
																			</span>
																			</td>
																			<td style="position: relative"><span
																				role="button" aria-haspopup="true"
																				aria-labelledby="display_message_fontsizeselect_text"
																				aria-describedby="display_message_fontsizeselect_voiceDesc"><table
																						role="presentation" tabindex="-1"
																						id="display_message_fontsizeselect"
																						cellpadding="0" cellspacing="0"
																						class="mceListBox mceListBoxEnabled mce_fontsizeselect"
																						aria-valuenow="Font size">
																						<tbody>
																							<tr>
																								<td class="mceFirst"><span
																									id="display_message_fontsizeselect_voiceDesc"
																									class="voiceLabel" style="display: none;">Font
																										size</span><a
																									id="display_message_fontsizeselect_text"
																									tabindex="-1" href="javascript:;"
																									class="mceText mceTitle"
																									onclick="return false;"
																									onmousedown="return false;">Font size</a>
																								</td>
																								<td class="mceLast"><a
																									id="display_message_fontsizeselect_open"
																									tabindex="-1" href="javascript:;"
																									class="mceOpen" onclick="return false;"
																									onmousedown="return false;"><span><span
																											style="display: none;" class="mceIconOnly"
																											aria-hidden="true">▼</span>
																									</span>
																								</a>
																								</td>
																							</tr>
																						</tbody>
																					</table>
																			</span>
																			</td>
																			<td style="position: relative"><span
																				role="button" aria-haspopup="true"
																				aria-labelledby="display_message_formatselect_text"
																				aria-describedby="display_message_formatselect_voiceDesc"><table
																						role="presentation" tabindex="-1"
																						id="display_message_formatselect" cellpadding="0"
																						cellspacing="0"
																						class="mceListBox mceListBoxEnabled mce_formatselect">
																						<tbody>
																							<tr>
																								<td class="mceFirst"><span
																									id="display_message_formatselect_voiceDesc"
																									class="voiceLabel" style="display: none;">Format</span><a
																									id="display_message_formatselect_text"
																									tabindex="-1" href="javascript:;"
																									class="mceText" onclick="return false;"
																									onmousedown="return false;">Format</a>
																								</td>
																								<td class="mceLast"><a
																									id="display_message_formatselect_open"
																									tabindex="-1" href="javascript:;"
																									class="mceOpen" onclick="return false;"
																									onmousedown="return false;"><span><span
																											style="display: none;" class="mceIconOnly"
																											aria-hidden="true">▼</span>
																									</span>
																								</a>
																								</td>
																							</tr>
																						</tbody>
																					</table>
																			</span>
																			</td>
																			<td style="position: relative"><span
																				class="mceSeparator" role="separator"
																				aria-orientation="vertical" tabindex="-1"></span>
																			</td>
																			<td style="position: relative"><span
																				role="button"
																				aria-labelledby="display_message_forecolor_voice"
																				aria-haspopup="true"><table
																						id="display_message_forecolor" role="presentation"
																						tabindex="-1"
																						class="mceSplitButton mceSplitButtonEnabled mce_forecolor"
																						cellpadding="0" cellspacing="0"
																						title="Select text color">
																						<tbody>
																							<tr>
																								<td class="mceFirst"><a role="button"
																									id="display_message_forecolor_action"
																									tabindex="-1" href="javascript:;"
																									class="mceAction mce_forecolor"
																									onclick="return false;"
																									onmousedown="return false;"
																									title="Select text color"><span
																										class="mceAction mce_forecolor"></span><span
																										class="mceVoiceLabel mceIconOnly"
																										id="display_message_forecolor_voice"
																										style="display: none;">Select text
																											color</span>
																									<div id="display_message_forecolor_preview"
																											class="mceColorPreview"
																											style="background-color: rgb(136, 136, 136);"></div>
																								</a>
																								</td>
																								<td class="mceLast"><a role="button"
																									id="display_message_forecolor_open"
																									tabindex="-1" href="javascript:;"
																									class="mceOpen mce_forecolor"
																									onclick="return false;"
																									onmousedown="return false;"
																									title="Select text color"><span
																										class="mceOpen mce_forecolor"><span
																											style="display: none;" class="mceIconOnly"
																											aria-hidden="true">▼</span>
																									</span>
																								</a>
																								</td>
																							</tr>
																						</tbody>
																					</table>
																			</span>
																			</td>
																			<td style="position: relative"><span
																				role="button"
																				aria-labelledby="display_message_backcolor_voice"
																				aria-haspopup="true"><table
																						id="display_message_backcolor" role="presentation"
																						tabindex="-1"
																						class="mceSplitButton mceSplitButtonEnabled mce_backcolor"
																						cellpadding="0" cellspacing="0"
																						title="Select background color">
																						<tbody>
																							<tr>
																								<td class="mceFirst"><a role="button"
																									id="display_message_backcolor_action"
																									tabindex="-1" href="javascript:;"
																									class="mceAction mce_backcolor"
																									onclick="return false;"
																									onmousedown="return false;"
																									title="Select background color"><span
																										class="mceAction mce_backcolor"></span><span
																										class="mceVoiceLabel mceIconOnly"
																										id="display_message_backcolor_voice"
																										style="display: none;">Select
																											background color</span>
																									<div id="display_message_backcolor_preview"
																											class="mceColorPreview"
																											style="background-color: rgb(136, 136, 136);"></div>
																								</a>
																								</td>
																								<td class="mceLast"><a role="button"
																									id="display_message_backcolor_open"
																									tabindex="-1" href="javascript:;"
																									class="mceOpen mce_backcolor"
																									onclick="return false;"
																									onmousedown="return false;"
																									title="Select background color"><span
																										class="mceOpen mce_backcolor"><span
																											style="display: none;" class="mceIconOnly"
																											aria-hidden="true">▼</span>
																									</span>
																								</a>
																								</td>
																							</tr>
																						</tbody>
																					</table>
																			</span>
																			</td>
																			<td class="mceToolbarEnd mceToolbarEndButton mceLast"><span>
																					<!-- IE -->
																			</span>
																			</td>
																		</tr>
																	</tbody>
																</table>
																<table id="display_message_toolbar2"
																	class="mceToolbar mceToolbarRow2 Enabled"
																	cellpadding="0" cellspacing="0" align=""
																	role="presentation" tabindex="-1" aria-disabled="false"
																	aria-pressed="false">
																	<tbody>
																		<tr>
																			<td
																				class="mceToolbarStart mceToolbarStartButton mceFirst"><span>
																					<!-- IE -->
																			</span>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_cut" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_cut"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_cut_voice"
																				title="Cut" tabindex="-1"><span
																					class="mceIcon mce_cut"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_cut_voice">Cut</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_copy" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_copy"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_copy_voice"
																				title="Copy" tabindex="-1"><span
																					class="mceIcon mce_copy"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_copy_voice">Copy</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_paste" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_paste"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_paste_voice"
																				title="Paste" tabindex="-1"><span
																					class="mceIcon mce_paste"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_paste_voice">Paste</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_pastetext" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_pastetext"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_pastetext_voice"
																				title="Paste as Plain Text" tabindex="-1"><span
																					class="mceIcon mce_pastetext"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_pastetext_voice">Paste
																						as Plain Text</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_pasteword" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_pasteword"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_pasteword_voice"
																				title="Paste from Word" tabindex="-1"><span
																					class="mceIcon mce_pasteword"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_pasteword_voice">Paste
																						from Word</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_charmap" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_charmap"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_charmap_voice"
																				title="Insert custom character" tabindex="-1"><span
																					class="mceIcon mce_charmap"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_charmap_voice">Insert
																						custom character</span>
																			</a>
																			</td>
																			<td style="position: relative"><span
																				class="mceSeparator" role="separator"
																				aria-orientation="vertical" tabindex="-1"></span>
																			</td>
																			<td style="position: relative"><span
																				role="button"
																				aria-labelledby="display_message_bullist_voice"
																				aria-haspopup="true"><table
																						id="display_message_bullist" role="presentation"
																						tabindex="-1"
																						class="mceSplitButton mceSplitButtonEnabled mce_bullist"
																						cellpadding="0" cellspacing="0"
																						title="Unordered list" aria-pressed="false">
																						<tbody>
																							<tr>
																								<td class="mceFirst"><a role="button"
																									id="display_message_bullist_action"
																									tabindex="-1" href="javascript:;"
																									class="mceAction mce_bullist"
																									onclick="return false;"
																									onmousedown="return false;"
																									title="Unordered list"><span
																										class="mceAction mce_bullist"></span><span
																										class="mceVoiceLabel mceIconOnly"
																										id="display_message_bullist_voice"
																										style="display: none;">Unordered list</span>
																								</a>
																								</td>
																								<td class="mceLast"><a role="button"
																									id="display_message_bullist_open" tabindex="-1"
																									href="javascript:;" class="mceOpen mce_bullist"
																									onclick="return false;"
																									onmousedown="return false;"
																									title="Unordered list"><span
																										class="mceOpen mce_bullist"><span
																											style="display: none;" class="mceIconOnly"
																											aria-hidden="true">▼</span>
																									</span>
																								</a>
																								</td>
																							</tr>
																						</tbody>
																					</table>
																			</span>
																			</td>
																			<td style="position: relative"><span
																				role="button"
																				aria-labelledby="display_message_numlist_voice"
																				aria-haspopup="true"><table
																						id="display_message_numlist" role="presentation"
																						tabindex="-1"
																						class="mceSplitButton mceSplitButtonEnabled mce_numlist"
																						cellpadding="0" cellspacing="0"
																						title="Ordered list" aria-pressed="false">
																						<tbody>
																							<tr>
																								<td class="mceFirst"><a role="button"
																									id="display_message_numlist_action"
																									tabindex="-1" href="javascript:;"
																									class="mceAction mce_numlist"
																									onclick="return false;"
																									onmousedown="return false;"
																									title="Ordered list"><span
																										class="mceAction mce_numlist"></span><span
																										class="mceVoiceLabel mceIconOnly"
																										id="display_message_numlist_voice"
																										style="display: none;">Ordered list</span>
																								</a>
																								</td>
																								<td class="mceLast"><a role="button"
																									id="display_message_numlist_open" tabindex="-1"
																									href="javascript:;" class="mceOpen mce_numlist"
																									onclick="return false;"
																									onmousedown="return false;"
																									title="Ordered list"><span
																										class="mceOpen mce_numlist"><span
																											style="display: none;" class="mceIconOnly"
																											aria-hidden="true">▼</span>
																									</span>
																								</a>
																								</td>
																							</tr>
																						</tbody>
																					</table>
																			</span>
																			</td>
																			<td style="position: relative"><span
																				class="mceSeparator" role="separator"
																				aria-orientation="vertical" tabindex="-1"></span>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_outdent" href="javascript:;"
																				class="mceButton mce_outdent mceButtonDisabled"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_outdent_voice"
																				title="Outdent" tabindex="-1" aria-disabled="true"><span
																					class="mceIcon mce_outdent"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_outdent_voice">Outdent</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_indent" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_indent"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_indent_voice"
																				title="Indent" tabindex="-1"><span
																					class="mceIcon mce_indent"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_indent_voice">Indent</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_blockquote" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_blockquote"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_blockquote_voice"
																				title="Blockquote" tabindex="-1"
																				aria-pressed="false"><span
																					class="mceIcon mce_blockquote"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_blockquote_voice">Blockquote</span>
																			</a>
																			</td>
																			<td style="position: relative"><span
																				class="mceSeparator" role="separator"
																				aria-orientation="vertical" tabindex="-1"></span>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_link" href="javascript:;"
																				class="mceButton mce_link mceButtonDisabled"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_link_voice"
																				title="Insert/edit link" tabindex="-1"
																				aria-disabled="true"><span
																					class="mceIcon mce_link"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_link_voice">Insert/edit
																						link</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_unlink" href="javascript:;"
																				class="mceButton mce_unlink mceButtonDisabled"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_unlink_voice"
																				title="Unlink" tabindex="-1" aria-disabled="true"><span
																					class="mceIcon mce_unlink"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_unlink_voice">Unlink</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_image" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_image"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_image_voice"
																				title="Insert/edit image" tabindex="-1"><span
																					class="mceIcon mce_image"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_image_voice">Insert/edit
																						image</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_media" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_media"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_media_voice"
																				title="Insert / edit embedded media" tabindex="-1"><span
																					class="mceIcon mce_media"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_media_voice">Insert /
																						edit embedded media</span>
																			</a>
																			</td>
																			<td style="position: relative"><span
																				class="mceSeparator" role="separator"
																				aria-orientation="vertical" tabindex="-1"></span>
																			</td>
																			<td style="position: relative"><span
																				class="mceSeparator" role="separator"
																				aria-orientation="vertical" tabindex="-1"></span>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_removeformat"
																				href="javascript:;"
																				class="mceButton mceButtonEnabled mce_removeformat"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_removeformat_voice"
																				title="Remove formatting" tabindex="-1"><span
																					class="mceIcon mce_removeformat"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_removeformat_voice">Remove
																						formatting</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_cleanup" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_cleanup"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_cleanup_voice"
																				title="Cleanup messy code" tabindex="-1"><span
																					class="mceIcon mce_cleanup"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_cleanup_voice">Cleanup
																						messy code</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_undo" href="javascript:;"
																				class="mceButton mce_undo mceButtonDisabled"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_undo_voice"
																				title="Undo (Ctrl+Z)" tabindex="-1"
																				aria-disabled="true"><span
																					class="mceIcon mce_undo"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_undo_voice">Undo
																						(Ctrl+Z)</span>
																			</a>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_redo" href="javascript:;"
																				class="mceButton mce_redo mceButtonDisabled"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_redo_voice"
																				title="Redo (Ctrl+Y)" tabindex="-1"
																				aria-disabled="true"><span
																					class="mceIcon mce_redo"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_redo_voice">Redo
																						(Ctrl+Y)</span>
																			</a>
																			</td>
																			<td style="position: relative"><span
																				class="mceSeparator" role="separator"
																				aria-orientation="vertical" tabindex="-1"></span>
																			</td>
																			<td style="position: relative"><a role="button"
																				id="display_message_code" href="javascript:;"
																				class="mceButton mceButtonEnabled mce_code"
																				onmousedown="return false;" onclick="return false;"
																				aria-labelledby="display_message_code_voice"
																				title="Edit HTML Source" tabindex="-1"><span
																					class="mceIcon mce_code"></span><span
																					class="mceVoiceLabel mceIconOnly"
																					style="display: none;"
																					id="display_message_code_voice">Edit HTML
																						Source</span>
																			</a>
																			</td>
																			<td class="mceToolbarEnd mceToolbarEndButton mceLast"><span>
																					<!-- IE -->
																			</span>
																			</td>
																		</tr>
																	</tbody>
																</table>
															</span>
														</div>
														<a href="#" accesskey="z"
														title="Jump to tool buttons - Alt+Q, Jump to editor - Alt-Z, Jump to element path - Alt-X"
														onfocus="tinyMCE.getInstanceById('display_message').focus();">
															<!-- IE -->
													</a>
													</td>
												</tr>
												<tr class="mceLast">
													<td class="mceIframeContainer mceFirst mceLast"><iframe
															id="display_message_ifr" src='javascript:""'
															frameborder="0"
															title="Rich Text Area. Press ALT F10 for toolbar. Press ALT 0 for help."
															style="width: 100%; height: 454px;"></iframe>
													</td>
												</tr>
											</tbody>
										</table>
									</span>
									<div class="preview">
										<h4>Preview on</h4>
										<ul>
											<li><a href="#" class="iphone">iPhone</a>
											</li>
											<li><a href="#" class="ipad">iPad</a>
											</li>
										</ul>
									</div>
								</fieldset>
								<div class="action">
									<span class="alert"></span> <img
										src="/resources/images/circle-loader.gif" class="loader">
									<button rel="back">Back</button>
									<button id="rp-step2-next" rel="next">
										Save and Continue <span class="next"></span>
									</button>
								</div>
							</div>
							<div class="step" id="step-2">
								<h2>
									Create a Push Notification <span class="sub message_title"></span>
								</h2>
								<div class="selection">
									<h3>
										<input type="radio" name="send_alert" value="0"
											id="alert-select-no"> <label for="alert-select-no">Don't
											Send a Notification</label>
									</h3>
									<p class="note">The Rich Push will be delivered but the
										user will not be alerted with a Push Notification.</p>
									<h3>
										<input type="radio" name="send_alert" value="1"
											id="alert-select-yes" checked="checked"> <label
											for="alert-select-yes">Send a Notification</label>
									</h3>
								</div>
								<div class="bare-box " id="push-alert-content">
									<fieldset>
										<label for="push_alert">Push Alert <span class="red">*</span>
										</label>
										<textarea name="push_alert" id="push_alert" rows="10"
											cols="100" maxlength="255"
											placeholder="Enter text for your push notification"></textarea>
										You have <span id="preview-chars">232</span> bytes left.
									</fieldset>
									<fieldset>
										<label for="push_sound">Push Sound</label> <input type="text"
											name="push_sound" id="push_sound" value=""
											placeholder="Enter the name of your custom alert sound">
									</fieldset>
									<fieldset>
										<div id="custom_fields1">
											<label for="push_key1">Key:</label><input id="push_key1"
												type="text" name="custom_key" class="custom_key"
												maxlength="255"> <label for="push_value1">Value:</label><input
												id="push_value1" type="text" name="custom_value"
												class="custom_value" maxlength="255"> <a
												class="button" id="kill_key1" href="javascript:kill_key(1)"><span>Remove</span>
											</a> <a class="button" id="add_fields1"
												href="javascript:moar_keys(1)"><span>Add another</span>
											</a>
										</div>
									</fieldset>
								</div>
								<div class="action">
									<span class="alert"></span> <img
										src="/resources/images/circle-loader.gif" class="loader">
									<button rel="back">Back</button>
									<button id="rp-step3-next" rel="next">
										Save and Continue <span class="next"></span>
									</button>
								</div>
							</div>
							<div class="step" id="step-3">
								<h2>
									Select Recipients <span class="sub message_title"></span>
								</h2>
								<div class="selection">
									<h3>
										<input type="radio" name="is_broadcast" value="1"
											id="select-recipient-all" checked="checked"> <label
											for="select-recipient-all">All Users (Broadcast)</label>
									</h3>
									<p class="note">All users registered with this application
										will receive this message.</p>
									<h3>
										<input type="radio" name="is_broadcast" value="0"
											id="select-recipient-some"> <label
											for="select-recipient-some">Some Users (By Tag)</label>
									</h3>
									<div class="bare-box disabled" id="recipient-some">
										<fieldset id="tag-search-box">
											<input type="text" name="tag_search" id="tag_search"
												placeholder="Enter tag" disabled="disabled" style="">
											<img src="/resources/images/circle-loader.gif"
												id="tag_loader">
											<ul id="tag-list" style="width: 600px; left: 0px;">
											</ul>
										</fieldset>
										<input type="hidden" name="tags" id="id_tags" value="">
										<div id="selected-tags"></div>
									</div>
								</div>
								<div class="action">
									<span class="alert"></span> <img
										src="/resources/images/circle-loader.gif" class="loader">
									<button rel="back">Back</button>
									<button id="rp-step4-next" rel="next">
										Save and Review <span class="next"></span>
									</button>
								</div>
							</div>
							<div class="step" id="step-4">
								<h2>
									Review <span class="sub message_title"></span>
								</h2>
								<div class="review-item">
									<button rel="change">Change</button>
									<h3>Rich Push</h3>
									<p>
										<strong>Title:</strong> <span id="display-title"></span>
									</p>
									<div class="preview">
										<h4>Preview on</h4>
										<ul>
											<li><a href="#" class="iphone">iPhone</a>
											</li>
											<li><a href="#" class="ipad">iPad</a>
											</li>
										</ul>
									</div>
								</div>
								<div class="review-item">
									<button rel="change">Change</button>
									<h3>Push Notification</h3>

									<p>
										<strong>Push Alert Text:</strong> <span
											id="display-push-alert">None</span>
									</p>
									<p>
										<strong>Push Sound:</strong> <span id="display-push-sound">None</span>
									</p>
									<p>
										<strong>Push Custom Keys:</strong>
									</p>
									<ul id="display-push-keys">
										<li>None</li>
									</ul>
								</div>
								<div class="review-item">
									<button rel="change">Change</button>
									<h3>Recipients</h3>
									<p id="display-recipients"></p>
								</div>
								<div class="action">
									<img src="/resources/images/circle-loader.gif" class="loader">
									<button rel="back">Back</button>
									<button id="rp-step5-next" rel="later">Save For Later</button>
									<button rel="send" type="submit">Send This Message Now</button>
								</div>
								<input type="hidden" name="payload" id="id_payload" value="">
								<input type="hidden" name="rich_push_history_id"
									id="id_rich_push_history_id" value=""> <input
									type="hidden" name="message" id="id_message" value="">
								<input type="hidden" name="ready_to_deliver"
									id="id_ready_to_deliver" value=""> <input type="hidden"
									name="save_for_later" id="id_save_for_later" value="">
								<input type="hidden" name="custom_keys" id="id_custom_keys"
									value="">
							</div>
						</form>
						<div class="step" id="step-5">
							<h2>
								Done <span class="sub message_title"></span>
							</h2>
						</div>
					</div>
				</div>

				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
</body>
</html>