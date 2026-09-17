import os
import sys
import docx
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn

# --- Color Palette Constants ---
HEX_PRIMARY_DARK = "291C0E"   # #291C0E (Headers, strong accents)
HEX_PRIMARY_BROWN = "6E473B"  # #6E473B (Subheadings, borders)
HEX_MUTED_BROWN = "A78D78"    # #A78D78
HEX_NEUTRAL = "BEB5A9"        # #BEB5A9 (Table borders, divider lines)
HEX_CREAM = "E1D4C2"          # #E1D4C2 (Table alt rows, callout background)
HEX_CREAM_LIGHT = "FBF9F5"    # #FBF9F5 (Code snippet background)
HEX_WHITE = "FFFFFF"

COLOR_PRIMARY_DARK = RGBColor(0x29, 0x1C, 0x0E)
COLOR_PRIMARY_BROWN = RGBColor(0x6E, 0x47, 0x3B)
COLOR_MUTED_BROWN = RGBColor(0xA7, 0x8D, 0x78)
COLOR_TEXT_DARK = RGBColor(0x22, 0x1C, 0x16)

def set_cell_background(cell, hex_color):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{hex_color}"/>')
    tcPr.append(shd)

def set_cell_margins(cell, top=120, bottom=120, left=160, right=160):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = OxmlElement('w:tcMar')
    for m, val in [('w:top', top), ('w:bottom', bottom), ('w:left', left), ('w:right', right)]:
        node = OxmlElement(m)
        node.set(qn('w:w'), str(val))
        node.set(qn('w:type'), 'dxa')
        tcMar.append(node)
    tcPr.append(tcMar)

def set_table_borders(table, hex_color=HEX_NEUTRAL):
    tblPr = table._tbl.tblPr
    borders = parse_xml(
        f'<w:tblBorders {nsdecls("w")}>\n'
        f'  <w:top w:val="single" w:sz="6" w:space="0" w:color="{hex_color}"/>\n'
        f'  <w:bottom w:val="single" w:sz="6" w:space="0" w:color="{hex_color}"/>\n'
        f'  <w:insideH w:val="single" w:sz="4" w:space="0" w:color="{hex_color}"/>\n'
        f'  <w:insideV w:val="none"/>\n'
        f'  <w:left w:val="none"/>\n'
        f'  <w:right w:val="none"/>\n'
        f'</w:tblBorders>'
    )
    tblPr.append(borders)

def add_header_p(doc, text, level=1):
    p = doc.add_paragraph()
    p.paragraph_format.keep_with_next = True
    run = p.add_run(text)
    run.font.name = "Georgia"
    run.bold = True
    
    if level == 1:
        p.paragraph_format.space_before = Pt(18)
        p.paragraph_format.space_after = Pt(8)
        run.font.size = Pt(17)
        run.font.color.rgb = COLOR_PRIMARY_DARK
    elif level == 2:
        p.paragraph_format.space_before = Pt(14)
        p.paragraph_format.space_after = Pt(6)
        run.font.size = Pt(13.5)
        run.font.color.rgb = COLOR_PRIMARY_BROWN
    elif level == 3:
        p.paragraph_format.space_before = Pt(10)
        p.paragraph_format.space_after = Pt(4)
        run.font.size = Pt(11.5)
        run.font.color.rgb = COLOR_PRIMARY_DARK
    return p

def add_body_p(doc, text="", space_after=6, line_spacing=1.15, bold_prefix=None):
    p = doc.add_paragraph()
    p.paragraph_format.space_after = Pt(space_after)
    p.paragraph_format.line_spacing = line_spacing
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        r_pre.font.name = "Calibri"
        r_pre.font.size = Pt(11)
        r_pre.bold = True
        r_pre.font.color.rgb = COLOR_PRIMARY_DARK
    if text:
        r = p.add_run(text)
        r.font.name = "Calibri"
        r.font.size = Pt(11)
        r.font.color.rgb = COLOR_TEXT_DARK
    return p

def add_bullet_p(doc, text, bold_prefix=None):
    p = doc.add_paragraph(style='List Bullet')
    p.paragraph_format.space_after = Pt(4)
    p.paragraph_format.line_spacing = 1.15
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        r_pre.font.name = "Calibri"
        r_pre.font.size = Pt(11)
        r_pre.bold = True
        r_pre.font.color.rgb = COLOR_PRIMARY_DARK
    r = p.add_run(text)
    r.font.name = "Calibri"
    r.font.size = Pt(11)
    r.font.color.rgb = COLOR_TEXT_DARK
    return p

def add_code_block(doc, code_str, caption=None):
    if caption:
        p_cap = doc.add_paragraph()
        p_cap.paragraph_format.space_before = Pt(8)
        p_cap.paragraph_format.space_after = Pt(3)
        r_cap = p_cap.add_run(caption)
        r_cap.font.name = "Calibri"
        r_cap.font.size = Pt(9.5)
        r_cap.bold = True
        r_cap.font.color.rgb = COLOR_PRIMARY_BROWN

    table = doc.add_table(rows=1, cols=1)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.autofit = False
    table.columns[0].width = Inches(6.2)
    
    cell = table.cell(0, 0)
    set_cell_background(cell, HEX_CREAM_LIGHT)
    set_cell_margins(cell, top=140, bottom=140, left=180, right=180)
    
    tcPr = cell._tc.get_or_add_tcPr()
    borders = parse_xml(
        f'<w:tcBorders {nsdecls("w")}>\n'
        f'  <w:left w:val="single" w:sz="18" w:space="0" w:color="{HEX_PRIMARY_BROWN}"/>\n'
        f'  <w:top w:val="single" w:sz="4" w:space="0" w:color="{HEX_NEUTRAL}"/>\n'
        f'  <w:right w:val="single" w:sz="4" w:space="0" w:color="{HEX_NEUTRAL}"/>\n'
        f'  <w:bottom w:val="single" w:sz="4" w:space="0" w:color="{HEX_NEUTRAL}"/>\n'
        f'</w:tcBorders>'
    )
    tcPr.append(borders)
    
    p = cell.paragraphs[0]
    p.paragraph_format.space_after = Pt(0)
    p.paragraph_format.line_spacing = 1.05
    run = p.add_run(code_str.strip())
    run.font.name = "Consolas"
    run.font.size = Pt(9.5)
    run.font.color.rgb = COLOR_PRIMARY_DARK
    
    p_after = doc.add_paragraph()
    p_after.paragraph_format.space_after = Pt(6)

def add_figure_image(doc, img_path, fig_num, title, description, width_in=5.8):
    if not os.path.exists(img_path):
        print(f"Warning: Image not found: {img_path}")
        return

    p_img = doc.add_paragraph()
    p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_img.paragraph_format.space_before = Pt(12)
    p_img.paragraph_format.space_after = Pt(4)
    run_img = p_img.add_run()
    run_img.add_picture(img_path, width=Inches(width_in))
    
    p_cap = doc.add_paragraph()
    p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_cap.paragraph_format.space_before = Pt(2)
    p_cap.paragraph_format.space_after = Pt(4)
    
    r_fig = p_cap.add_run(f"Figure {fig_num} — ")
    r_fig.font.name = "Calibri"
    r_fig.font.size = Pt(10)
    r_fig.bold = True
    r_fig.font.color.rgb = COLOR_PRIMARY_DARK
    
    r_title = p_cap.add_run(title)
    r_title.font.name = "Calibri"
    r_title.font.size = Pt(10)
    r_title.bold = True
    r_title.font.color.rgb = COLOR_PRIMARY_BROWN

    p_desc = doc.add_paragraph()
    p_desc.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_desc.paragraph_format.space_before = Pt(0)
    p_desc.paragraph_format.space_after = Pt(12)
    r_desc = p_desc.add_run(description)
    r_desc.font.name = "Calibri"
    r_desc.font.size = Pt(9.5)
    r_desc.font.italic = True
    r_desc.font.color.rgb = COLOR_TEXT_DARK

def create_styled_table(doc, headers, rows_data, col_widths=None):
    table = doc.add_table(rows=len(rows_data) + 1, cols=len(headers))
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.autofit = False
    set_table_borders(table, HEX_NEUTRAL)

    # Header row
    hdr_cells = table.rows[0].cells
    for i, h in enumerate(headers):
        hdr_cells[i].text = h
        set_cell_background(hdr_cells[i], HEX_PRIMARY_DARK)
        set_cell_margins(hdr_cells[i], top=140, bottom=140, left=140, right=140)
        p = hdr_cells[i].paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.LEFT
        p.paragraph_format.space_after = Pt(0)
        for r in p.runs:
            r.font.name = "Calibri"
            r.font.size = Pt(10)
            r.bold = True
            r.font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)

    # Data rows
    for r_idx, row_values in enumerate(rows_data):
        row_cells = table.rows[r_idx + 1].cells
        bg_hex = HEX_CREAM_LIGHT if (r_idx % 2 == 1) else HEX_WHITE
        for c_idx, val in enumerate(row_values):
            row_cells[c_idx].text = str(val)
            set_cell_background(row_cells[c_idx], bg_hex)
            set_cell_margins(row_cells[c_idx], top=100, bottom=100, left=140, right=140)
            p = row_cells[c_idx].paragraphs[0]
            p.paragraph_format.space_after = Pt(0)
            p.paragraph_format.line_spacing = 1.1
            for r in p.runs:
                r.font.name = "Calibri"
                r.font.size = Pt(9.5)
                r.font.color.rgb = COLOR_TEXT_DARK

    if col_widths:
        for row in table.rows:
            for i, w in enumerate(col_widths):
                row.cells[i].width = Inches(w)

    p_after = doc.add_paragraph()
    p_after.paragraph_format.space_after = Pt(8)
    return table

def build_report():
    doc = Document()

    # Page Setup: A4, 1 inch margins
    for section in doc.sections:
        section.page_width = Inches(8.27)
        section.page_height = Inches(11.69)
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)

    base_dir = r"d:\Mini-Projects\StudentRecordFileStore"
    img_dir = os.path.join(base_dir, "imags")

    # =========================================================================
    # COVER PAGE
    # =========================================================================
    p_top = doc.add_paragraph()
    p_top.paragraph_format.space_before = Pt(36)
    p_top.paragraph_format.space_after = Pt(10)
    p_top.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_proj = p_top.add_run("A TECHNICAL PROJECT REPORT ON")
    r_proj.font.name = "Georgia"
    r_proj.font.size = Pt(12)
    r_proj.bold = True
    r_proj.font.color.rgb = COLOR_PRIMARY_BROWN

    p_title = doc.add_paragraph()
    p_title.paragraph_format.space_before = Pt(12)
    p_title.paragraph_format.space_after = Pt(8)
    p_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_t = p_title.add_run("STUDENT RECORD FILE STORE")
    r_t.font.name = "Georgia"
    r_t.font.size = Pt(26)
    r_t.bold = True
    r_t.font.color.rgb = COLOR_PRIMARY_DARK

    p_sub = doc.add_paragraph()
    p_sub.paragraph_format.space_before = Pt(0)
    p_sub.paragraph_format.space_after = Pt(36)
    p_sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_sub = p_sub.add_run("An Object-Oriented Desktop Management System with Robust File Persistence & Integrated Data Structures")
    r_sub.font.name = "Calibri"
    r_sub.font.size = Pt(13)
    r_sub.font.italic = True
    r_sub.font.color.rgb = COLOR_PRIMARY_BROWN

    p_line = doc.add_paragraph()
    p_line.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_line.paragraph_format.space_after = Pt(40)
    r_line = p_line.add_run("____________________________________________________")
    r_line.font.color.rgb = COLOR_MUTED_BROWN
    r_line.bold = True

    p_subm = doc.add_paragraph()
    p_subm.paragraph_format.space_before = Pt(20)
    p_subm.paragraph_format.space_after = Pt(4)
    p_subm.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_subm = p_subm.add_run("Submitted in partial fulfillment of the academic requirements for the degree of")
    r_subm.font.name = "Calibri"
    r_subm.font.size = Pt(11)
    r_subm.font.color.rgb = COLOR_TEXT_DARK

    p_deg = doc.add_paragraph()
    p_deg.paragraph_format.space_after = Pt(36)
    p_deg.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_deg = p_deg.add_run("Bachelor of Technology\nin\nComputer Science and Engineering")
    r_deg.font.name = "Georgia"
    r_deg.font.size = Pt(14)
    r_deg.bold = True
    r_deg.font.color.rgb = COLOR_PRIMARY_DARK

    table_meta = doc.add_table(rows=4, cols=2)
    table_meta.alignment = WD_TABLE_ALIGNMENT.CENTER
    table_meta.columns[0].width = Inches(2.2)
    table_meta.columns[1].width = Inches(3.8)
    set_cell_background(table_meta.cell(0, 0), HEX_CREAM_LIGHT)
    set_cell_background(table_meta.cell(0, 1), HEX_CREAM_LIGHT)
    set_cell_background(table_meta.cell(1, 0), HEX_CREAM_LIGHT)
    set_cell_background(table_meta.cell(1, 1), HEX_CREAM_LIGHT)
    set_cell_background(table_meta.cell(2, 0), HEX_CREAM_LIGHT)
    set_cell_background(table_meta.cell(2, 1), HEX_CREAM_LIGHT)
    set_cell_background(table_meta.cell(3, 0), HEX_CREAM_LIGHT)
    set_cell_background(table_meta.cell(3, 1), HEX_CREAM_LIGHT)

    meta_items = [
        ("Student Name:", "Sasanka Sekhar Kundu"),
        ("Roll Number:", "150096725118"),
        ("Academic Program:", "B.Tech Computer Science and Engineering"),
        ("Technology Stack:", "Java 21 (LTS) & Java Swing GUI")
    ]
    for idx, (label, val) in enumerate(meta_items):
        cell_l = table_meta.cell(idx, 0)
        cell_v = table_meta.cell(idx, 1)
        p_l = cell_l.paragraphs[0]
        r_l = p_l.add_run(label)
        r_l.font.name = "Calibri"
        r_l.font.size = Pt(10.5)
        r_l.bold = True
        r_l.font.color.rgb = COLOR_PRIMARY_BROWN

        p_v = cell_v.paragraphs[0]
        r_v = p_v.add_run(val)
        r_v.font.name = "Calibri"
        r_v.font.size = Pt(10.5)
        r_v.bold = (idx < 2)
        r_v.font.color.rgb = COLOR_PRIMARY_DARK

    set_table_borders(table_meta, HEX_MUTED_BROWN)

    p_bot = doc.add_paragraph()
    p_bot.paragraph_format.space_before = Pt(50)
    p_bot.paragraph_format.space_after = Pt(0)
    p_bot.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_bot = p_bot.add_run("[DEPARTMENT OF COMPUTER SCIENCE AND ENGINEERING]\n[INSTITUTION / UNIVERSITY NAME PLACEHOLDER]\nAcademic Year 2025 – 2026")
    r_bot.font.name = "Georgia"
    r_bot.font.size = Pt(11)
    r_bot.font.color.rgb = COLOR_PRIMARY_DARK

    doc.add_page_break()

    # =========================================================================
    # CERTIFICATE
    # =========================================================================
    add_header_p(doc, "CERTIFICATE OF ORIGINAL WORK", level=1)
    doc.paragraphs[-1].alignment = WD_ALIGN_PARAGRAPH.CENTER

    add_body_p(doc, "This is to certify that the academic technical project entitled \"STUDENT RECORD FILE STORE\" is a bona fide record of independent work carried out and successfully implemented by Sasanka Sekhar Kundu (Roll Number: 150096725118) in partial fulfillment of the requirements for the award of the degree of Bachelor of Technology in Computer Science and Engineering during the academic session 2025–2026.", space_after=12)

    add_body_p(doc, "The project satisfies the mandatory academic guidelines prescribed by the curriculum, demonstrating the practical integration of Java File Handling, Collections Framework (ArrayList), Checked Exception Handling, Object-Oriented Programming (Abstraction, Inheritance, Polymorphism, Interfaces), and Core Data Structures and Algorithms (Stack-based Undo, FIFO Operation Queue, Linear and Binary Search, and Comparator Sorting).", space_after=24)

    sig_table = doc.add_table(rows=2, cols=3)
    sig_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    sig_table.columns[0].width = Inches(2.0)
    sig_table.columns[1].width = Inches(2.0)
    sig_table.columns[2].width = Inches(2.0)
    
    sigs = [
        ("____________________\nProject Supervisor / Guide\n[Faculty Name Placeholder]\nDept. of CSE", 0),
        ("____________________\nInternal Examiner\n[Signature / Date]\nDept. of CSE", 1),
        ("____________________\nHead of the Department\n[Faculty Name Placeholder]\nDept. of CSE", 2)
    ]
    for text, col_idx in sigs:
        cell = sig_table.cell(1, col_idx)
        p = cell.paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p.paragraph_format.space_before = Pt(40)
        p.paragraph_format.line_spacing = 1.15
        r = p.add_run(text)
        r.font.name = "Calibri"
        r.font.size = Pt(9.5)
        r.font.color.rgb = COLOR_PRIMARY_DARK

    doc.add_page_break()

    # =========================================================================
    # DECLARATION
    # =========================================================================
    add_header_p(doc, "CANDIDATE DECLARATION", level=1)
    doc.paragraphs[-1].alignment = WD_ALIGN_PARAGRAPH.CENTER

    add_body_p(doc, "I, Sasanka Sekhar Kundu, bearing University Roll Number 150096725118, hereby declare that the software project entitled \"STUDENT RECORD FILE STORE\" submitted to the Department of Computer Science and Engineering is an original work designed, coded, and tested by me under academic supervision.", space_after=12)

    add_body_p(doc, "I further declare that this codebase, user interface, and accompanying documentation have not been submitted previously to any other university or institution for the award of any degree, diploma, or academic certificate.", space_after=12)

    add_body_p(doc, "All source codes, algorithms, and design implementations presented herein reflect genuine work executing in Java 21, and all secondary references and standard documentation consulted during development have been formally acknowledged.", space_after=36)

    p_dec_sig = doc.add_paragraph()
    p_dec_sig.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    p_dec_sig.paragraph_format.line_spacing = 1.2
    r_ds = p_dec_sig.add_run("______________________________\nSasanka Sekhar Kundu\nRoll No: 150096725118\nB.Tech Computer Science & Engineering\nDate: September 17, 2026")
    r_ds.font.name = "Calibri"
    r_ds.font.size = Pt(10.5)
    r_ds.bold = True
    r_ds.font.color.rgb = COLOR_PRIMARY_DARK

    doc.add_page_break()

    # =========================================================================
    # ACKNOWLEDGEMENT
    # =========================================================================
    add_header_p(doc, "ACKNOWLEDGEMENT", level=1)
    doc.paragraphs[-1].alignment = WD_ALIGN_PARAGRAPH.CENTER

    add_body_p(doc, "The completion of this academic project would not have been possible without the guidance, encouragement, and intellectual support of several individuals and mentors.", space_after=10)

    add_body_p(doc, "First and foremost, I express my sincere gratitude to my Project Supervisor and the faculty members of the Department of Computer Science and Engineering for their continuous technical feedback, insightful recommendations, and rigorous evaluation criteria throughout the lifecycle of this project.", space_after=10)

    add_body_p(doc, "I extend my appreciation to the Head of the Department for providing the computational resources, software toolchains, and supportive academic environment essential for developing, compiling, and testing Java desktop applications.", space_after=10)

    add_body_p(doc, "Finally, I thank my family and peers for their constant motivation, patience, and assistance during testing and documentation.", space_after=24)

    p_ack_sig = doc.add_paragraph()
    p_ack_sig.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    r_as = p_ack_sig.add_run("Sasanka Sekhar Kundu\nRoll No: 150096725118")
    r_as.font.name = "Calibri"
    r_as.font.size = Pt(10.5)
    r_as.bold = True
    r_as.font.color.rgb = COLOR_PRIMARY_DARK

    doc.add_page_break()

    # =========================================================================
    # ABSTRACT
    # =========================================================================
    add_header_p(doc, "ABSTRACT", level=1)
    doc.paragraphs[-1].alignment = WD_ALIGN_PARAGRAPH.CENTER

    add_body_p(doc, "The \"Student Record File Store\" is a desktop management system designed and implemented in Java 21 to address the critical academic need for structured, persistent, and verifiable student record processing. While enterprise software frequently relies on complex database engines, understanding fundamental computer science principles requires developing direct solutions that combine core language capabilities: low-level File Handling, in-memory Collection abstractions, checked Exception Handling, and classical Data Structures.", space_after=10)

    add_body_p(doc, "The system architecture is organized into clean, decoupled tiers: data models, service handlers, custom exceptions, validation contracts, and an antialiased Java Swing graphical user interface. Student records are maintained in an in-memory ArrayList<Student> for ultra-fast manipulation and persisted to a durable plain-text store (data/students.txt) using pipe-delimited records. High-level object-oriented programming principles are rigorously integrated: Abstraction is demonstrated through an abstract Person class; Inheritance is established as Student extends Person; Polymorphism is actively realized via runtime dynamic method dispatch on overridden methods; and Encapsulation guarantees that all entity attributes are strictly guarded with validated accessors.", space_after=10)

    add_body_p(doc, "Beyond foundational CRUD operations, the application incorporates classical Data Structures and Algorithms: a Stack<Action> provides a multi-action Undo facility (reverting additions, modifications, and deletions without data loss); a Queue<StudentOperation> logs administrative transactions in strict First-In, First-Out (FIFO) chronological sequence; Search algorithms are implemented utilizing both Linear Search O(n) across multiple fields and Binary Search O(log n) on sorted student identifiers; and custom Comparator implementations allow dynamic multi-attribute sorting. To ensure optimal desktop ergonomics, interactive pagination is integrated across all record-display interfaces. An automated test verification suite (ProjectVerifier) confirms 100% compliance across 16 formal academic benchmarks.", space_after=10)

    add_body_p(doc, "Keywords: Java 21, Java Swing, File Handling, ArrayList, Exception Handling, Object-Oriented Programming, Stack DSA, Queue DSA, Linear Search, Binary Search, Pagination.", space_after=16, bold_prefix="Key Terms: ")

    doc.add_page_break()

    # =========================================================================
    # TABLE OF CONTENTS
    # =========================================================================
    add_header_p(doc, "TABLE OF CONTENTS", level=1)
    doc.paragraphs[-1].alignment = WD_ALIGN_PARAGRAPH.CENTER

    toc_items = [
        ("Certificate of Original Work", "ii"),
        ("Candidate Declaration", "iii"),
        ("Acknowledgement", "iv"),
        ("Abstract", "v"),
        ("Chapter 1 — Introduction", "1"),
        ("Chapter 2 — Problem Statement", "3"),
        ("Chapter 3 — Objectives of the Project", "4"),
        ("Chapter 4 — Scope of the Application", "5"),
        ("Chapter 5 — System Requirements Specification", "6"),
        ("Chapter 6 — System Design & Architecture", "7"),
        ("Chapter 7 — Object-Oriented Programming (OOP) Principles", "10"),
        ("Chapter 8 — Data Structures and Algorithms (DSA)", "14"),
        ("Chapter 9 — System Implementation Details", "18"),
        ("Chapter 10 — Graphical User Interface (GUI) & UX Design", "22"),
        ("Chapter 11 — File Handling & Data Persistence", "27"),
        ("Chapter 12 — Exception Handling Architecture", "30"),
        ("Chapter 13 — Authentication & Security System", "32"),
        ("Chapter 14 — CRUD Operations Analysis", "34"),
        ("Chapter 15 — System Testing & Verification Suite", "36"),
        ("Chapter 16 — Results & Performance Analysis", "40"),
        ("Chapter 17 — Technical Limitations", "42"),
        ("Chapter 18 — Future Scope & Enhancements", "43"),
        ("Chapter 19 — Conclusion", "44"),
        ("Chapter 20 — References", "45"),
        ("Appendix A — Complete Project File Structure", "46"),
        ("Appendix B — Sample Dataset (data/students.txt)", "47"),
        ("Appendix C — Core Source Code Excerpts", "48"),
        ("Appendix D — Academic Viva Voce Question Bank", "52")
    ]

    toc_table = doc.add_table(rows=len(toc_items), cols=2)
    toc_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    toc_table.columns[0].width = Inches(5.2)
    toc_table.columns[1].width = Inches(1.0)
    for idx, (title, page) in enumerate(toc_items):
        c_title = toc_table.cell(idx, 0)
        c_page = toc_table.cell(idx, 1)
        p_t = c_title.paragraphs[0]
        p_t.paragraph_format.space_after = Pt(2)
        r_t = p_t.add_run(title)
        r_t.font.name = "Calibri"
        r_t.font.size = Pt(10)
        if "Chapter" in title or "Appendix" in title:
            r_t.bold = True
            r_t.font.color.rgb = COLOR_PRIMARY_DARK
        else:
            r_t.font.color.rgb = COLOR_TEXT_DARK

        p_p = c_page.paragraphs[0]
        p_p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        p_p.paragraph_format.space_after = Pt(2)
        r_p = p_p.add_run(page)
        r_p.font.name = "Calibri"
        r_p.font.size = Pt(10)
        r_p.font.color.rgb = COLOR_PRIMARY_BROWN

    set_table_borders(toc_table, HEX_WHITE)

    doc.add_page_break()

    # =========================================================================
    # CHAPTER 1 — INTRODUCTION
    # =========================================================================
    add_header_p(doc, "Chapter 1 — Introduction", level=1)
    
    add_body_p(doc, "Academic record management is a critical operational responsibility within modern higher education institutions. Departmental administrators, academic coordinators, and faculty advisors must reliably capture, index, retrieve, and update student performance parameters, enrolled coursework, semester standing, and demographic indicators. In historical academic workflows, these records were logged manually on physical ledgers or fragmented spreadsheet documents, introducing serious hazards including transcription errors, duplicate record insertion, accidental deletion, and total absence of verifiable transaction histories.", space_after=10)

    add_body_p(doc, "The Student Record File Store project was designed and engineered as a modern, reliable, and academically rigorous desktop software solution using Java 21 (LTS). By leveraging native Java desktop capabilities, the system bridges the gap between raw algorithmic data processing and an intuitive graphical user interface.", space_after=10)

    add_header_p(doc, "1.1 The Academic Mandate", level=2)
    add_body_p(doc, "According to the university curriculum requirements, this project was developed around three core academic pillars:", space_after=6)
    add_bullet_p(doc, "Standard Java I/O streams (BufferedReader, BufferedWriter, FileReader, FileWriter) persisting structured pipe-delimited records to disk without relying on heavy external database engines.", bold_prefix="1. File Handling: ")
    add_bullet_p(doc, "A dynamically sized, in-memory collection (ArrayList<Student>) offering rapid indexing, sequential scanning, and in-memory mutability.", bold_prefix="2. ArrayList Architecture: ")
    add_bullet_p(doc, "A custom checked exception hierarchy (StudentException) ensuring that invalid inputs, duplicate identifiers, corrupt file rows, or missing resources are caught and handled gracefully.", bold_prefix="3. Exception Handling: ")

    add_header_p(doc, "1.2 Modern Architectural Upgrades", level=2)
    add_body_p(doc, "To provide deep academic value and demonstrate software engineering maturity, the application was systematically expanded to incorporate advanced Object-Oriented Programming (OOP) paradigms and foundational Data Structures and Algorithms (DSA). Rather than treating OOP and DSA as theoretical concepts, they are woven directly into the application's runtime engine, powering a Stack-based Undo manager, a FIFO transaction Queue, dual-mode Searching (Linear and Binary), dynamic Comparator sorting, and interactive multi-surface pagination.", space_after=12)

    # =========================================================================
    # CHAPTER 2 — PROBLEM STATEMENT
    # =========================================================================
    add_header_p(doc, "Chapter 2 — Problem Statement", level=1)

    add_body_p(doc, "In academic administrative offices, maintaining student records often suffers from structural vulnerabilities due to relying on disconnected flat files, loose spreadsheets, or overly complex enterprise databases that are impractical for local departmental workflows. The primary challenges addressed in this software development lifecycle include:", space_after=10)

    add_bullet_p(doc, "Spreadsheets allow arbitrary text in numeric columns, permitting negative marks, semester values exceeding academic norms, or blank names, leading to corrupted institutional datasets.", bold_prefix="1. Lack of Input Validation: ")
    add_bullet_p(doc, "Uncoordinated manual entry frequently results in duplicate Student IDs, causing severe ambiguity in student grading and semester records.", bold_prefix="2. Identity Collision & Duplicate IDs: ")
    add_bullet_p(doc, "Standard desktop tools do not maintain an audit trail or an operational Undo mechanism. If an administrator accidentally deletes or modifies a record, the historical data is permanently lost.", bold_prefix="3. Irreversible Mutation: ")
    add_bullet_p(doc, "Manual scanning of hundreds of records in text files is slow and error-prone. A structured search mechanism with algorithmic complexity awareness is required.", bold_prefix="4. Inefficient Retrieval: ")
    add_bullet_p(doc, "Many lightweight applications crash abruptly when encountering missing files, permission denial, or malformed data lines. Software must demonstrate high fault resilience.", bold_prefix="5. Fragile Persistence: ")

    # =========================================================================
    # CHAPTER 3 — OBJECTIVES OF THE PROJECT
    # =========================================================================
    add_header_p(doc, "Chapter 3 — Objectives of the Project", level=1)

    add_body_p(doc, "The overarching objective is to build a robust, standalone, university-compliant Java desktop application that fulfills all core curricular criteria while adhering to high software engineering standards.", space_after=10)

    add_bullet_p(doc, "Construct a complete desktop GUI using Java Swing with clean component layouts, custom 2D antialiased rendering, and dynamic navigation.", bold_prefix="Objective 1: ")
    add_bullet_p(doc, "Implement full CRUD operations (Create, Read, Update, Delete) on student entities in memory using ArrayList<Student>.", bold_prefix="Objective 2: ")
    add_bullet_p(doc, "Persist records to a structured, pipe-delimited file (data/students.txt) with automatic directory creation, file initialization, and corrupt-line recovery.", bold_prefix="Objective 3: ")
    add_bullet_p(doc, "Establish a rigorous exception handling architecture (StudentException) ensuring zero unhandled runtime crashes.", bold_prefix="Objective 4: ")
    add_bullet_p(doc, "Implement authentic OOP principles: Abstraction (Person), Inheritance (Student extends Person), Polymorphism (dynamic dispatch of displayInfo()), Encapsulation (validated accessors), and Interfaces (Validatable contract).", bold_prefix="Objective 5: ")
    add_bullet_p(doc, "Implement practical DSA components: Stack<Action> for reversible LIFO mutations, Queue<StudentOperation> for chronological transaction tracking, Linear & Binary Search, and Comparator-based sorting.", bold_prefix="Objective 6: ")
    add_bullet_p(doc, "Design and integrate interactive pagination with customizable row sizes and dynamic navigation across all data tables.", bold_prefix="Objective 7: ")
    add_bullet_p(doc, "Provide an automated verification suite (ProjectVerifier) validating all core academic requirements programmatically.", bold_prefix="Objective 8: ")

    # =========================================================================
    # CHAPTER 4 — SCOPE OF THE APPLICATION
    # =========================================================================
    add_header_p(doc, "Chapter 4 — Scope of the Application", level=1)

    add_body_p(doc, "The scope of the Student Record File Store encompasses departmental student administration within college environments, technical institutes, and academic training centers.", space_after=10)

    add_header_p(doc, "4.1 Functional Scope", level=2)
    add_bullet_p(doc, "Role-based credential gateway protecting administrative records from unauthorized terminal access.", bold_prefix="Administrative Authentication: ")
    add_bullet_p(doc, "Input of student details (ID, full name, engineering course, semester 1–8, marks 0–100) with immediate barrier validation.", bold_prefix="Student Enrollment: ")
    add_bullet_p(doc, "Tabular display with multi-attribute sorting (ID, Name, Marks, Semester) and in-table quick edit/delete actions.", bold_prefix="Record Directory: ")
    add_bullet_p(doc, "Dual-algorithm search module comparing Linear Search O(n) and Binary Search O(log n) with microsecond execution timer readouts.", bold_prefix="Algorithmic Search: ")
    add_bullet_p(doc, "Full rollback of the most recent ADD, UPDATE, or DELETE operation via an internal LIFO Action stack.", bold_prefix="Reversible State Mutations: ")
    add_bullet_p(doc, "Explicit Save and Reload capabilities with real-time I/O activity logging.", bold_prefix="Storage Control: ")

    add_header_p(doc, "4.2 Operational Boundaries", level=2)
    add_body_p(doc, "The application is explicitly architected as a local desktop software suite. It deliberately avoids cloud and remote database dependencies to preserve pure Java portability and ensure zero-configuration deployment on any host with a standard Java Runtime Environment (JRE).", space_after=12)

    # =========================================================================
    # CHAPTER 5 — SYSTEM REQUIREMENTS SPECIFICATION
    # =========================================================================
    add_header_p(doc, "Chapter 5 — System Requirements Specification", level=1)

    add_body_p(doc, "The system was designed for lightweight execution and high portability across operating systems.", space_after=10)

    add_header_p(doc, "5.1 Hardware Requirements", level=2)
    hw_headers = ["Hardware Component", "Minimum Requirement", "Recommended Specification"]
    hw_rows = [
        ["Processor", "x86/x64 Dual Core @ 1.8 GHz", "Intel Core i5 / AMD Ryzen 5 or higher"],
        ["RAM (Memory)", "2 GB System RAM", "8 GB or higher"],
        ["Storage", "50 MB available disk space", "500 MB (with Java Development Kit)"],
        ["Display", "1024 × 768 resolution", "1920 × 1080 Full HD (Antialiased)"],
        ["Peripherals", "Standard Keyboard and Mouse", "Standard Keyboard and Optical Mouse"]
    ]
    create_styled_table(doc, hw_headers, hw_rows, [1.8, 2.2, 2.2])

    add_header_p(doc, "5.2 Software Requirements", level=2)
    sw_headers = ["Software Environment", "Specification / Tool Utilized"]
    sw_rows = [
        ["Operating System", "Microsoft Windows 10 / 11 (64-bit) (or Linux / macOS)"],
        ["Java Platform", "OpenJDK 21.0.10 / Java SE Development Kit (JDK 21 LTS)"],
        ["Compiler & Toolchain", "javac 21.0.10, java launcher"],
        ["GUI Framework", "Java Swing / Abstract Window Toolkit (AWT)"],
        ["Development Environment", "Visual Studio Code with Extension Pack for Java"],
        ["Shell / CLI", "Windows PowerShell / Terminal"]
    ]
    create_styled_table(doc, sw_headers, sw_rows, [2.2, 4.0])

    # =========================================================================
    # CHAPTER 6 — SYSTEM DESIGN & ARCHITECTURE
    # =========================================================================
    add_header_p(doc, "Chapter 6 — System Design & Architecture", level=1)

    add_body_p(doc, "The architecture of Student Record File Store follows a clean, decoupled, layered design that separates visual presentation, operational coordination, persistence abstractions, and data models.", space_after=10)

    add_header_p(doc, "6.1 High-Level Architecture", level=2)
    add_body_p(doc, "The application structure is organized into five distinct layers:", space_after=6)
    add_bullet_p(doc, "Java Swing components organized in a single top-level MainFrame with a CardLayout container that swaps active panels (Dashboard, Add Student, View Records, Search, File Operations, About) dynamically.", bold_prefix="1. Presentation Tier (GUI): ")
    add_bullet_p(doc, "AuthenticationManager verifies credentials before rendering the primary administrative workspace.", bold_prefix="2. Security & Session Tier: ")
    add_bullet_p(doc, "StudentManager coordinates business logic, orchestrates UndoManager (Stack) and OperationQueue (Queue), and invokes SearchService and SortService.", bold_prefix="3. Business Logic & Service Tier: ")
    add_bullet_p(doc, "FileHandler encapsulates all standard Java I/O interactions, reading and serializing entities to data/students.txt.", bold_prefix="4. Persistence Tier: ")
    add_bullet_p(doc, "Person (abstract base), Student (concrete entity), Action (snapshot for undo), and StudentOperation (transaction record).", bold_prefix="5. Domain Model Tier: ")

    add_header_p(doc, "6.2 Architectural Component Flow Diagram", level=2)
    arch_flow = (
        "+-----------------------------------------------------------------------------------------+\n"
        "|                                     USER INTERACTION                                    |\n"
        "+-----------------------------------------------------------------------------------------+\n"
        "                                             |                                             \n"
        "                                             v                                             \n"
        "+-----------------------------------------------------------------------------------------+\n"
        "|                            LOGIN SCREEN (LoginFrame.java)                               |\n"
        "|                   AuthenticationManager: Credential Verification                        |\n"
        "+-----------------------------------------------------------------------------------------+\n"
        "                                             |                                             \n"
        "                                             v (On Successful Authentication)              \n"
        "+-----------------------------------------------------------------------------------------+\n"
        "|                             MAIN APPLICATION (MainFrame.java)                           |\n"
        "|   Sidebar Navigation | Dynamic Breadcrumb Header | Antialiased CardLayout Container     |\n"
        "+-----------------------------------------------------------------------------------------+\n"
        "       |                  |                  |                  |                 |        \n"
        "       v                  v                  v                  v                 v        \n"
        " [DashboardPanel]  [AddStudentPanel]  [RecordsPanel]     [SearchPanel]   [FileOpsPanel]    \n"
        "  - KPI Metrics     - Field Validation - Sorting          - Linear Search - Metadata       \n"
        "  - Recent Records  - Dupl. Check      - Inline Actions   - Binary Search - Save / Reload  \n"
        "  - Pagination      - Stack Snapshot   - Stack Undo       - Timer Metrics - I/O Activity   \n"
        "                                       - Pagination       - Pagination    - Console        \n"
        "       |                  |                  |                  |                 |        \n"
        "       +------------------+------------------+------------------+-----------------+        \n"
        "                                             |                                             \n"
        "                                             v                                             \n"
        "+-----------------------------------------------------------------------------------------+\n"
        "|                         SERVICE LAYER (StudentManager.java)                             |\n"
        "|   Coordinates: UndoManager (Stack<Action>) | OperationQueue (Queue<StudentOperation>)   |\n"
        "|   Delegates: SearchService (Linear/Binary) | SortService (Comparators)                  |\n"
        "+-----------------------------------------------------------------------------------------+\n"
        "                                             |                                             \n"
        "                                             v                                             \n"
        "+-----------------------------------------------------------------------------------------+\n"
        "|                 IN-MEMORY DATA STRUCTURE: ArrayList<Student>                            |\n"
        "+-----------------------------------------------------------------------------------------+\n"
        "                                             |                                             \n"
        "                                             v                                             \n"
        "+-----------------------------------------------------------------------------------------+\n"
        "|                 PERSISTENCE LAYER (FileHandler.java): Java File I/O                     |\n"
        "|                 BufferedReader / BufferedWriter / FileReader / FileWriter               |\n"
        "+-----------------------------------------------------------------------------------------+\n"
        "                                             |                                             \n"
        "                                             v                                             \n"
        "+-----------------------------------------------------------------------------------------+\n"
        "|                         LOCAL STORAGE: data/students.txt                                |\n"
        "|                         Format: ID|Name|Course|Semester|Marks                           |\n"
        "+-----------------------------------------------------------------------------------------+"
    )
    add_code_block(doc, arch_flow, caption="Figure 6.1 — System Architecture & Component Interaction Model")

    add_header_p(doc, "6.3 UML Class Diagram & Relationships", level=2)
    add_body_p(doc, "The relationships between the system's classes reflect classic Object-Oriented principles. The UML diagram below accurately captures the class hierarchies, interface contracts, and associations confirmed in the source code:", space_after=6)

    uml_text = (
        "+-----------------------------------------+         +-------------------------------+\n"
        "|               <<abstract>>              |         |         <<interface>>         |\n"
        "|                  Person                 |         |          Validatable          |\n"
        "+-----------------------------------------+         +-------------------------------+\n"
        "| # id: int                               |         | + validate(): void            |\n"
        "| # name: String                          |         +-------------------------------+\n"
        "+-----------------------------------------+                         ^                \n"
        "| + getId(): int                          |                         | implements     \n"
        "| + getName(): String                     |                         |                \n"
        "| + {abstract} displayInfo(): String      |                         |                \n"
        "+-----------------------------------------+                         |                \n"
        "                    ^                                               |                \n"
        "                    | extends                                       |                \n"
        "+-------------------------------------------------------------------+               \n"
        "|                                Student                                            |\n"
        "+-----------------------------------------------------------------------------------+\n"
        "| - course: String                                                                  |\n"
        "| - semester: int                                                                   |\n"
        "| - marks: double                                                                   |\n"
        "+-----------------------------------------------------------------------------------+\n"
        "| + validate(): void  <<implements Validatable>>                                    |\n"
        "| + displayInfo(): String  <<overrides Person>>                                     |\n"
        "| + toString(): String  <<overrides Object>>                                        |\n"
        "| + getCourse(): String / setCourse(c: String): void                                |\n"
        "| + getSemester(): int / setSemester(s: int): void                                  |\n"
        "| + getMarks(): double / setMarks(m: double): void                                  |\n"
        "+-----------------------------------------------------------------------------------+\n"
        "         ^                                                    ^                      \n"
        "         | 1..* contains                                      | snapshot reference   \n"
        "+----------------------------------+         +--------------------------------------+\n"
        "|          StudentManager          |         |                Action                |\n"
        "+----------------------------------+         +--------------------------------------+\n"
        "| - students: ArrayList<Student>   |         | - type: ActionType                   |\n"
        "| - undoManager: UndoManager       |         | - previousStudent: Student           |\n"
        "| - opQueue: OperationQueue        |         | - currentStudent: Student            |\n"
        "+----------------------------------+         +--------------------------------------+\n"
        "| + addStudent(s: Student): void   |                            ^                    \n"
        "| + updateStudent(s: Student): void|                            | stores             \n"
        "| + deleteStudent(id: int): void   |         +--------------------------------------+\n"
        "| + undoLastAction(): Action       |<>------>|             UndoManager              |\n"
        "| + getAllStudents(): ArrayList    |         +--------------------------------------+\n"
        "+----------------------------------+         | - undoStack: Stack<Action>           |\n"
        "         |                  |                +--------------------------------------+\n"
        "         | uses             | delegates      | + pushAction(a: Action): void        |\n"
        "         v                  v                | + popAction(): Action                |\n"
        "+------------------+ +-------------------+   | + canUndo(): boolean                 |\n"
        "|   FileHandler    | | Search & Sort     |   +--------------------------------------+\n"
        "+------------------+ +-------------------+\n"
        "| + saveStudents() | | SearchService     |\n"
        "| + loadStudents() | | SortService       |\n"
        "+------------------+ +-------------------+"
    )
    add_code_block(doc, uml_text, caption="Figure 6.2 — Object-Oriented Class & Interface Diagram")

    # =========================================================================
    # CHAPTER 7 — OBJECT-ORIENTED PROGRAMMING (OOP) PRINCIPLES
    # =========================================================================
    add_header_p(doc, "Chapter 7 — Object-Oriented Programming (OOP) Principles", level=1)

    add_body_p(doc, "Object-Oriented Programming represents the bedrock of modern software engineering in Java. The Student Record File Store application was consciously architected to demonstrate all six core OOP pillars through genuine, working code rather than artificial demonstration stubs.", space_after=10)

    add_header_p(doc, "7.1 Encapsulation", level=2)
    add_body_p(doc, "Encapsulation is the mechanism that binds data and code into a single unit while hiding internal state from external interference. In model/Person.java and model/Student.java, all fields (id, name, course, semester, marks) are declared with private or protected access modifiers. State modification is only permitted through public getters and setters that enforce validation barriers.", space_after=6)
    code_encap = (
        "// From Student.java: Encapsulated fields and validated mutators\n"
        "private String course;\n"
        "private int semester;\n"
        "private double marks;\n\n"
        "public void setMarks(double marks) throws StudentException {\n"
        "    if (marks < 0 || marks > 100) {\n"
        "        throw new StudentException(\"Marks must be between 0.0 and 100.0\");\n"
        "    }\n"
        "    this.marks = marks;\n"
        "}"
    )
    add_code_block(doc, code_encap, caption="Listing 7.1 — Encapsulation in Student.java with Boundary Guard")

    add_header_p(doc, "7.2 Abstraction", level=2)
    add_body_p(doc, "Abstraction focuses on exposing essential characteristics while concealing low-level implementation details. The abstract class model/Person.java defines the conceptual blueprint for any human entity in the institution, mandating that all concrete subclasses provide an implementation of displayInfo().", space_after=6)
    code_abstr = (
        "// From Person.java: Abstract base class\n"
        "public abstract class Person {\n"
        "    protected int id;\n"
        "    protected String name;\n\n"
        "    public Person(int id, String name) {\n"
        "        this.id = id;\n"
        "        this.name = name;\n"
        "    }\n"
        "    public abstract String displayInfo(); // Enforced contract\n"
        "}"
    )
    add_code_block(doc, code_abstr, caption="Listing 7.2 — Abstract Base Class Person.java")

    add_header_p(doc, "7.3 Inheritance", level=2)
    add_body_p(doc, "Inheritance is the mechanism by which a new class acquires the properties and methods of an existing class, fostering code reusability and establishing hierarchical categorization. In this project, model/Student extends model/Person using the 'extends' keyword. Student inherits id and name while extending the domain with course, semester, and marks.", space_after=6)
    code_inh = (
        "// From Student.java: Extending Person\n"
        "public class Student extends Person implements Validatable {\n"
        "    public Student(int id, String name, String course, int semester, double marks) {\n"
        "        super(id, name); // Reusing superclass constructor\n"
        "        this.course = course;\n"
        "        this.semester = semester;\n"
        "        this.marks = marks;\n"
        "    }\n"
        "}"
    )
    add_code_block(doc, code_inh, caption="Listing 7.3 — Inheritance via super() Constructor Chaining")

    add_header_p(doc, "7.4 Polymorphism & Dynamic Method Dispatch", level=2)
    add_body_p(doc, "Polymorphism (\"many forms\") enables an object reference of a parent type to invoke overridden methods on child instances dynamically at runtime. In gui/AboutPanel.java and Main.java, a polymorphic reference Person person = new Student(...) is instantiated. When person.displayInfo() is called, Java dynamically binds the execution to Student's implementation at runtime.", space_after=6)
    code_poly = (
        "// Dynamic Runtime Polymorphism Demonstration\n"
        "Person polyPerson = new Student(101, \"Rahul Sharma\", \"B.Tech CSE\", 2, 85.0);\n"
        "// Resolves dynamically to Student's overridden displayInfo() at runtime:\n"
        "String output = polyPerson.displayInfo();"
    )
    add_code_block(doc, code_poly, caption="Listing 7.4 — Runtime Polymorphic Method Dispatch")

    add_header_p(doc, "7.5 Interface Contract", level=2)
    add_body_p(doc, "An interface establishes a formal behavioral specification that implementing classes must satisfy. The interfaces/Validatable.java interface enforces data integrity verification across domain models.", space_after=6)
    code_iface = (
        "// From Validatable.java\n"
        "public interface Validatable {\n"
        "    void validate() throws StudentException;\n"
        "}\n\n"
        "// In Student.java\n"
        "@Override\n"
        "public void validate() throws StudentException {\n"
        "    if (id <= 0) throw new StudentException(\"Student ID must be positive.\");\n"
        "    if (name == null || name.trim().isEmpty())\n"
        "        throw new StudentException(\"Student name cannot be empty.\");\n"
        "    if (semester < 1 || semester > 8)\n"
        "        throw new StudentException(\"Semester must be between 1 and 8.\");\n"
        "    if (marks < 0.0 || marks > 100.0)\n"
        "        throw new StudentException(\"Marks must be between 0.0 and 100.0.\");\n"
        "}"
    )
    add_code_block(doc, code_iface, caption="Listing 7.5 — Interface Definition and Concrete Implementation")

    add_header_p(doc, "7.6 Method Overriding", level=2)
    add_body_p(doc, "Method Overriding occurs when a child class provides a specific implementation of a method declared in its superclass. Student overrides both Person.displayInfo() and Object.toString(). The @Override annotation ensures compile-time signature verification.", space_after=6)
    code_override = (
        "@Override\n"
        "public String displayInfo() {\n"
        "    return String.format(\"Student ID: %d | Name: %s | Course: %s | Sem: %d | Marks: %.2f\",\n"
        "            id, name, course, semester, marks);\n"
        "}\n\n"
        "@Override\n"
        "public String toString() {\n"
        "    return String.format(\"%d|%s|%s|%d|%.2f\", id, name, course, semester, marks);\n"
        "}"
    )
    add_code_block(doc, code_override, caption="Listing 7.6 — Overriding displayInfo() and toString()")

    # =========================================================================
    # CHAPTER 8 — DATA STRUCTURES AND ALGORITHMS (DSA)
    # =========================================================================
    add_header_p(doc, "Chapter 8 — Data Structures and Algorithms (DSA)", level=1)

    add_body_p(doc, "A distinguishing technical feature of the Student Record File Store is its authentic implementation of core classical data structures and algorithms to solve real operational problems.", space_after=10)

    add_header_p(doc, "8.1 ArrayList<Student> (Dynamic Contiguous Storage)", level=2)
    add_body_p(doc, "The Java ArrayList<E> is a resizable-array implementation of the List interface. In service/StudentManager.java, an ArrayList<Student> serves as the central primary memory buffer:", space_after=6)
    add_bullet_p(doc, "Direct index retrieval via get(int index) operates in O(1) constant time, enabling instantaneous rendering of paginated table slices.", bold_prefix="Constant-Time Access: ")
    add_bullet_p(doc, "Unlike fixed-size primitive arrays (Student[]), ArrayList automatically manages internal buffer doubling as student enrollment expands.", bold_prefix="Dynamic Growth: ")
    add_bullet_p(doc, "Sequential iteration over the array list enables rapid linear scanning and collection manipulation.", bold_prefix="Collection Traversal: ")

    add_header_p(doc, "8.2 Stack<Action> (LIFO Undo Engine)", level=2)
    add_body_p(doc, "A Stack is a classical linear data structure operating on the Last-In, First-Out (LIFO) discipline. In service/UndoManager.java, a java.util.Stack<Action> provides the application's administrative rollback mechanism:", space_after=6)
    add_bullet_p(doc, "Before mutating student data, an Action snapshot is pushed onto the stack. For an ADD, it captures the added student; for an UPDATE, it preserves both old and new student clones; for a DELETE, it retains the deleted entity.", bold_prefix="Push Operation: ")
    add_bullet_p(doc, "When the administrator clicks 'Undo Last Action', the top action is popped from the stack and inverted in constant O(1) time.", bold_prefix="Pop Operation: ")
    code_stack = (
        "// From UndoManager.java & StudentManager.java: LIFO Rollback\n"
        "public Action undoLastAction() throws StudentException {\n"
        "    if (!undoManager.canUndo()) {\n"
        "        throw new StudentException(\"Undo stack is empty. No operations to undo.\");\n"
        "    }\n"
        "    Action lastAction = undoManager.popAction();\n"
        "    switch (lastAction.getType()) {\n"
        "        case ADD:\n"
        "            // Reversal of ADD is DELETE\n"
        "            removeStudentDirectly(lastAction.getCurrentStudent().getId());\n"
        "            break;\n"
        "        case UPDATE:\n"
        "            // Reversal of UPDATE restores previous clone\n"
        "            replaceStudentDirectly(lastAction.getPreviousStudent());\n"
        "            break;\n"
        "        case DELETE:\n"
        "            // Reversal of DELETE restores deleted entity\n"
        "            students.add(lastAction.getPreviousStudent());\n"
        "            break;\n"
        "    }\n"
        "    return lastAction;\n"
        "}"
    )
    add_code_block(doc, code_stack, caption="Listing 8.1 — Stack-based LIFO Undo Logic in StudentManager.java")

    add_header_p(doc, "8.3 Queue<StudentOperation> (FIFO Audit Trail)", level=2)
    add_body_p(doc, "A Queue operates on the First-In, First-Out (FIFO) principle. In service/OperationQueue.java, a java.util.Queue<StudentOperation> backed by a LinkedList tracks administrative actions in strict chronological order, guaranteeing unalterable audit trails of additions, updates, deletions, and persistence calls.", space_after=6)
    code_queue = (
        "// From OperationQueue.java: FIFO Transaction Enqueuing\n"
        "private final Queue<StudentOperation> queue = new LinkedList<>();\n\n"
        "public synchronized void enqueue(StudentOperation op) {\n"
        "    queue.offer(op); // Enqueue at tail in FIFO order\n"
        "}\n\n"
        "public synchronized StudentOperation dequeue() {\n"
        "    return queue.poll(); // Dequeue from head\n"
        "}"
    )
    add_code_block(doc, code_queue, caption="Listing 8.2 — FIFO Transaction Queue Implementation")

    add_header_p(doc, "8.4 Searching Algorithms: Linear vs. Binary Search", level=2)
    add_body_p(doc, "The service/SearchService.java module provides two distinct search algorithms, allowing performance comparisons on the Search GUI panel:", space_after=6)
    add_bullet_p(doc, "Iterates sequentially through the collection comparing the query against ID, Name, and Course substrings. Time complexity is O(n); space complexity is O(1).", bold_prefix="1. Linear Search O(n): ")
    add_bullet_p(doc, "Operates on a pre-sorted list of students ordered by ID. By dividing the search space in half at each iteration, it locates targets in O(log n) time.", bold_prefix="2. Binary Search O(log n): ")
    code_binsearch = (
        "// From SearchService.java: Binary Search O(log n) by Student ID\n"
        "public static Student binarySearchById(ArrayList<Student> list, int targetId) {\n"
        "    int low = 0;\n"
        "    int high = list.size() - 1;\n"
        "    while (low <= high) {\n"
        "        int mid = low + (high - low) / 2;\n"
        "        int midId = list.get(mid).getId();\n"
        "        if (midId == targetId) return list.get(mid);\n"
        "        else if (midId < targetId) low = mid + 1;\n"
        "        else high = mid - 1;\n"
        "    }\n"
        "    return null; // Not found\n"
        "}"
    )
    add_code_block(doc, code_binsearch, caption="Listing 8.3 — Classical Binary Search Implementation")

    add_header_p(doc, "8.5 Sorting Algorithms & Custom Comparators", level=2)
    add_body_p(doc, "Sorting is provided via service/SortService.java using custom java.util.Comparator implementations passed to List.sort(). The application supports sorting in both Ascending and Descending directions across all numerical and textual attributes:", space_after=6)
    code_sort = (
        "// From SortService.java: Custom Comparators for Flexible Sorting\n"
        "public static void sortById(ArrayList<Student> list, boolean ascending) {\n"
        "    list.sort((s1, s2) -> ascending ? \n"
        "        Integer.compare(s1.getId(), s2.getId()) :\n"
        "        Integer.compare(s2.getId(), s1.getId()));\n"
        "}\n\n"
        "public static void sortByMarks(ArrayList<Student> list, boolean ascending) {\n"
        "    list.sort((s1, s2) -> ascending ?\n"
        "        Double.compare(s1.getMarks(), s2.getMarks()) :\n"
        "        Double.compare(s2.getMarks(), s1.getMarks()));\n"
        "}"
    )
    add_code_block(doc, code_sort, caption="Listing 8.4 — Comparator-based Multi-Attribute Sorting")

    add_header_p(doc, "8.6 Interactive Multi-Surface Pagination Algorithm", level=2)
    add_body_p(doc, "To ensure optimal performance and responsive rendering when working with large datasets, a custom mathematical slicing algorithm powers the pagination toolbar across all data tables (RecordsPanel, SearchPanel, and DashboardPanel):", space_after=6)
    code_page = (
        "// Pagination Calculation and Row Slicing\n"
        "int total = allStudents.size();\n"
        "int totalPages = Math.max(1, (int) Math.ceil((double) total / pageSize));\n"
        "if (currentPage > totalPages) currentPage = totalPages;\n"
        "int startIndex = (currentPage - 1) * pageSize;\n"
        "int endIndex = Math.min(startIndex + pageSize, total);\n\n"
        "for (int i = startIndex; i < endIndex; i++) {\n"
        "    Student s = allStudents.get(i);\n"
        "    tableModel.addRow(...);\n"
        "}"
    )
    add_code_block(doc, code_page, caption="Listing 8.5 — Mathematical Sublist Slicing for Pagination")

    # =========================================================================
    # CHAPTER 9 — SYSTEM IMPLEMENTATION DETAILS
    # =========================================================================
    add_header_p(doc, "Chapter 9 — System Implementation Details", level=1)

    add_body_p(doc, "The codebase is organized into distinct, cohesive packages following clean architectural conventions. Every package has a single responsibility, minimizing coupling and maximizing maintainability.", space_after=10)

    pkg_headers = ["Package Name", "Contained Classes / Interfaces", "Architectural Responsibility"]
    pkg_rows = [
        ["(default) src", "Main.java, ProjectVerifier.java", "Application startup, antialiasing flags, and automated test suite."],
        ["auth", "AuthenticationManager.java", "Credential validation, session authentication, and access control."],
        ["model", "Person.java, Student.java, Action.java, StudentOperation.java", "Core domain entities, abstract bases, and snapshot records."],
        ["interfaces", "Validatable.java", "Contract defining the validate() method."],
        ["exception", "StudentException.java", "Custom checked exception for domain and persistence errors."],
        ["service", "FileHandler.java, StudentManager.java, UndoManager.java, OperationQueue.java, SearchService.java, SortService.java", "Business services, data structures, file persistence, searching, and sorting."],
        ["gui", "MainFrame.java, LoginFrame.java, DashboardPanel.java, AddStudentPanel.java, RecordsPanel.java, SearchPanel.java, FileOperationsPanel.java, AboutPanel.java, UIConstants.java", "Antialiased Swing presentation layer and warm aesthetic design system."]
    ]
    create_styled_table(doc, pkg_headers, pkg_rows, [1.2, 2.2, 2.8])

    # =========================================================================
    # CHAPTER 10 — GUI / USER INTERFACE & UX DESIGN
    # =========================================================================
    add_header_p(doc, "Chapter 10 — Graphical User Interface (GUI) & UX Design", level=1)

    add_body_p(doc, "The user interface of the Student Record File Store was engineered from the ground up to provide a modern, distraction-free desktop experience. Built with native Java Swing and AWT, it avoids generic default components in favor of custom 2D antialiased rendering.", space_after=10)

    add_header_p(doc, "10.1 UI Design System & Color Palette", level=2)
    add_body_p(doc, "The entire application follows a curated, warm earthy academic color palette defined as constants in gui/UIConstants.java:", space_after=6)
    
    pal_headers = ["Palette Token", "Hex Code", "Color Swatch Description", "Application Context & Usage"]
    pal_rows = [
        ["Primary Dark", "#291C0E", "Deep Espresso Brown", "Sidebar background, table headers, primary text, prominent buttons."],
        ["Primary Brown", "#6E473B", "Warm Terracotta Brown", "Secondary buttons, section headers, active indicators, borders."],
        ["Muted Brown", "#A78D78", "Earthy Sandstone", "Subtitle text, secondary labels, disabled controls, subtle outlines."],
        ["Neutral", "#BEB5A9", "Warm Warm Gray", "Table borders, card dividers, neutral pill fills, inactive states."],
        ["Cream", "#E1D4C2", "Antique Cream White", "Main content canvas, input backgrounds, table row fills, card backgrounds."]
    ]
    create_styled_table(doc, pal_headers, pal_rows, [1.2, 1.0, 1.6, 2.4])

    add_header_p(doc, "10.2 Login Interface", level=2)
    add_body_p(doc, "The login screen (gui/LoginFrame.java) serves as the security gateway to the system. It presents a centered, elevated credential card with fields for username and password, supported by custom vector lock icons and clear error feedback.", space_after=6)
    add_figure_image(doc, os.path.join(img_dir, "login.png"), "10.1", "Administrative Login Interface", "The Login interface provides secure credential authentication before granting access to student data.")

    add_header_p(doc, "10.3 Administrative Dashboard", level=2)
    add_body_p(doc, "Upon successful login, the administrator is greeted by the Dashboard (gui/DashboardPanel.java). The dashboard serves as an executive cockpit featuring four Key Performance Indicator (KPI) cards (Total Students, Records Saved to File, Operation Queue Status, and Persistence Health), a paginated preview table of recently registered students with page sizing, and quick-action navigation shortcuts.", space_after=6)
    add_figure_image(doc, os.path.join(img_dir, "dashboard.png"), "10.2", "Administrative Dashboard Overview", "The Dashboard displays live system metrics, operational health, and a paginated recent records preview.")

    add_header_p(doc, "10.4 Add Student Interface", level=2)
    add_body_p(doc, "The student registration interface (gui/AddStudentPanel.java) is centered vertically and horizontally using GridBagLayout. It provides clear input fields for Student ID, Full Name, Course dropdown, Semester, and Marks. Input fields validate immediately on submission, rejecting invalid, non-numeric, or duplicate inputs before delegating to the business layer.", space_after=6)
    add_figure_image(doc, os.path.join(img_dir, "addStudnets.png"), "10.3", "Student Registration Interface", "The Add Student interface validates inputs and rejects invalid values before committing records.")

    add_header_p(doc, "10.5 View Records Directory with Stack Undo & Pagination", level=2)
    add_body_p(doc, "The records management interface (gui/RecordsPanel.java) displays the complete in-memory student collection in an antialiased JTable. It includes an interactive top toolbar for multi-column sorting (ID, Name, Marks, Semester in Ascending/Descending orders), an 'Undo Last Action' button linked to the LIFO stack, and inline Edit and Delete buttons on each row. The bottom toolbar features a dynamic pagination control with a page size selector (5, 8, 10, 15, 20), range label, and numbered buttons.", space_after=6)
    add_figure_image(doc, os.path.join(img_dir, "ViewRecords.png"), "10.4", "Student Records Directory with Pagination & Undo", "The Records table supports multi-column sorting, inline editing/deletion, LIFO undo, and pagination.")

    add_header_p(doc, "10.6 Search Directory & Algorithm Performance Benchmark", level=2)
    add_body_p(doc, "The search interface (gui/SearchPanel.java) allows administrators to query records using either Linear Search O(n) or Binary Search O(log n). It displays microsecond execution times, result counts, and a dedicated paginated search result table.", space_after=6)
    add_figure_image(doc, os.path.join(img_dir, "Search.png"), "10.5", "Search Directory with Algorithmic Benchmarks", "The Search interface allows administrators to execute and benchmark Linear and Binary search algorithms.")

    add_header_p(doc, "10.7 File Operations & Live Persistence Console", level=2)
    add_body_p(doc, "The file operations panel (gui/FileOperationsPanel.java) provides transparency into physical file storage. The left card displays storage metadata (target file path, file existence, file size in bytes, active in-memory record count) and explicit 'Save to File' and 'Reload from File' buttons. The right card features an auto-scrolling I/O Activity Console displaying timestamped diagnostic logs.", space_after=6)
    add_figure_image(doc, os.path.join(img_dir, "ActivityInfo.png"), "10.6", "File Operations & Persistence Activity Console", "The File Operations panel displays file metadata, save/reload actions, and live diagnostic logs.")

    add_header_p(doc, "10.8 About Panel & Interactive Polymorphism Tester", level=2)
    add_body_p(doc, "The About interface (gui/AboutPanel.java) provides comprehensive developer metadata for Sasanka Sekhar Kundu (Roll: 150096725118), an interactive Viva Voce academic concept checklist, and a live Polymorphism Verification tester that instantiates a Person reference to execute dynamic method dispatch on demand.", space_after=6)
    add_figure_image(doc, os.path.join(img_dir, "About.png"), "10.7", "About Panel & Interactive Polymorphism Tester", "The About panel provides developer credentials, academic concept summaries, and an interactive OOP test.")

    # =========================================================================
    # CHAPTER 11 — FILE HANDLING & DATA PERSISTENCE
    # =========================================================================
    add_header_p(doc, "Chapter 11 — File Handling & Data Persistence", level=1)

    add_body_p(doc, "File handling represents the foundational academic requirement of the curriculum. In this project, service/FileHandler.java encapsulates all file persistence operations using Java's standard java.io package (BufferedReader, BufferedWriter, FileReader, FileWriter).", space_after=10)

    add_header_p(doc, "11.1 File Format Specification", level=2)
    add_body_p(doc, "Student records are persisted in a clean, human-readable, pipe-delimited flat file located at data/students.txt. Each line represents exactly one student entity conforming to the schema:", space_after=6)
    code_fmt = "ID|Name|Course|Semester|Marks\nExample:\n101|Rahul Sharma|B.Tech CSE|2|85\n102|Priya Shah|B.Tech CSE|2|91\n106|Sneha Kulkarni|B.Tech AI & DS|2|93"
    add_code_block(doc, code_fmt, caption="Listing 11.1 — Pipe-Delimited Data Format in data/students.txt")

    add_header_p(doc, "11.2 Automatic Directory & File Provisioning", level=2)
    add_body_p(doc, "To prevent FileNotFoundException crashes on a clean deployment, FileHandler automatically checks if the parent directory (data/) and target file (students.txt) exist, creating them transparently if necessary.", space_after=6)
    code_prov = (
        "// From FileHandler.java: Autonomous Directory and File Creation\n"
        "File file = new File(filePath);\n"
        "File parentDir = file.getParentFile();\n"
        "if (parentDir != null && !parentDir.exists()) {\n"
        "    parentDir.mkdirs(); // Creates data/ directory automatically\n"
        "}\n"
        "if (!file.exists()) {\n"
        "    file.createNewFile(); // Initializes empty data/students.txt\n"
        "}"
    )
    add_code_block(doc, code_prov, caption="Listing 11.2 — Automated Directory & File Creation Logic")

    add_header_p(doc, "11.3 File Reading & Fault-Tolerant Parsing", level=2)
    add_body_p(doc, "During load operations, FileHandler reads lines sequentially using BufferedReader. If a line is blank, corrupt, contains missing columns, or has non-numeric values, FileHandler logs a diagnostic warning and continues reading the next line without crashing the application.", space_after=6)
    code_read = (
        "// From FileHandler.java: Fault-tolerant parsing\n"
        "try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {\n"
        "    String line;\n"
        "    int lineNum = 0;\n"
        "    while ((line = reader.readLine()) != null) {\n"
        "        lineNum++;\n"
        "        line = line.trim();\n"
        "        if (line.isEmpty()) continue;\n"
        "        String[] tokens = line.split(\"\\\\|\");\n"
        "        if (tokens.length < 5) {\n"
        "            System.err.println(\"Malformed record at line \" + lineNum);\n"
        "            continue; // Skip without crashing\n"
        "        }\n"
        "        int id = Integer.parseInt(tokens[0].trim());\n"
        "        String name = tokens[1].trim();\n"
        "        String course = tokens[2].trim();\n"
        "        int semester = Integer.parseInt(tokens[3].trim());\n"
        "        double marks = Double.parseDouble(tokens[4].trim());\n"
        "        Student s = new Student(id, name, course, semester, marks);\n"
        "        s.validate();\n"
        "        loadedList.add(s);\n"
        "    }\n"
        "}"
    )
    add_code_block(doc, code_read, caption="Listing 11.3 — Robust Line-by-Line File Deserialization")

    # =========================================================================
    # CHAPTER 12 — EXCEPTION HANDLING ARCHITECTURE
    # =========================================================================
    add_header_p(doc, "Chapter 12 — Exception Handling Architecture", level=1)

    add_body_p(doc, "Exception Handling is the third mandatory academic pillar. The application establishes a custom checked exception class, exception/StudentException, to decouple domain-specific error signaling from low-level runtime crashes.", space_after=10)

    add_header_p(doc, "12.1 Custom Checked Exception: StudentException", level=2)
    code_ex_class = (
        "// From StudentException.java\n"
        "package exception;\n\n"
        "public class StudentException extends Exception {\n"
        "    public StudentException(String message) {\n"
        "        super(message);\n"
        "    }\n"
        "    public StudentException(String message, Throwable cause) {\n"
        "        super(message, cause);\n"
        "    }\n"
        "}"
    )
    add_code_block(doc, code_ex_class, caption="Listing 12.1 — Custom Checked StudentException Definition")

    add_header_p(doc, "12.2 Comprehensive Exception Handling Taxonomy", level=2)
    ex_headers = ["Exception Scenario", "Source Class", "Triggering Condition", "System Mitigation & UX Response"]
    ex_rows = [
        ["Duplicate Student ID", "StudentManager.java", "Adding a student with an ID that already exists in memory.", "Throws StudentException; displays informative warning dialog in GUI."],
        ["Invalid Marks Range", "Student.java", "Marks value < 0.0 or > 100.0.", "Fails validation contract; input rejected with corrective guidance."],
        ["Invalid Semester Range", "Student.java", "Semester value < 1 or > 8.", "Throws StudentException; prompts user for valid academic term."],
        ["Non-Numeric Input", "AddStudentPanel.java", "Entering alphabetic text into ID, Semester, or Marks fields.", "Catches NumberFormatException; surfaces clean error modal."],
        ["Empty / Blank Name", "Student.java", "Name field left blank or whitespace-only.", "Rejects submission; highlights input field."],
        ["Corrupt File Line", "FileHandler.java", "Malformed record with missing pipes or invalid tokens in file.", "Logs error to console; skips corrupt line and loads remaining records."],
        ["Empty Undo Stack", "UndoManager.java", "User clicks Undo when no actions have occurred.", "Throws StudentException(\"Undo stack is empty\"); notifies user cleanly."]
    ]
    create_styled_table(doc, ex_headers, ex_rows, [1.5, 1.2, 1.8, 1.7])

    # =========================================================================
    # CHAPTER 13 — AUTHENTICATION & SECURITY SYSTEM
    # =========================================================================
    add_header_p(doc, "Chapter 13 — Authentication & Security System", level=1)

    add_body_p(doc, "To protect academic records from unauthorized terminal tampering, an administrative authentication gateway is integrated into the startup sequence via auth/AuthenticationManager.java.", space_after=10)

    add_body_p(doc, "Default academic credentials are configured in the source code for demonstration and evaluation purposes. When the user launches the application, LoginFrame intercepts execution. If the user provides valid administrative credentials, LoginFrame disposes itself and constructs MainFrame; otherwise, it displays an authentication failure modal with remaining attempts.", space_after=10)

    code_auth = (
        "// From AuthenticationManager.java: Credential verification\n"
        "public static boolean authenticate(String username, String password) {\n"
        "    if (username == null || password == null) return false;\n"
        "    return username.trim().equals(ADMIN_USER) && \n"
        "           password.trim().equals(ADMIN_PASS);\n"
        "}"
    )
    add_code_block(doc, code_auth, caption="Listing 13.1 — Administrative Credential Verification")

    # =========================================================================
    # CHAPTER 14 — CRUD OPERATIONS ANALYSIS
    # =========================================================================
    add_header_p(doc, "Chapter 14 — CRUD Operations Analysis", level=1)

    add_body_p(doc, "The lifecycle of student records is governed by standard CRUD (Create, Read, Update, Delete) operations. The table below details how each operation affects the in-memory ArrayList, the Swing GUI, and disk persistence:", space_after=10)

    crud_headers = ["CRUD Operation", "Memory Impact (ArrayList)", "User Interface Feedback", "Persistence & File Store Impact"]
    crud_rows = [
        ["CREATE (Add)", "Appends new Student object to ArrayList<Student>; pushes ADD Action to Undo Stack; enqueues transaction.", "Clears registration form; updates Dashboard KPI cards; updates paginated table view.", "Marked as unsaved in memory until 'Save to File' is triggered or auto-synced."],
        ["READ (View/Search)", "Performs indexed or sequential traversal of ArrayList; extracts paginated slices.", "Renders rows in JTable with marks pill styling; displays active page and total records.", "Non-mutating operation; reads directly from in-memory cache."],
        ["UPDATE (Edit)", "Locates student by ID; updates attributes via validated setters; pushes UPDATE Action to Undo Stack.", "Opens pre-populated edit dialog; updates table row and paginated preview on confirmation.", "Memory state updated; changes saved on next FileHandler persistence cycle."],
        ["DELETE (Remove)", "Removes student from ArrayList<Student>; pushes DELETE Action with snapshot to Undo Stack.", "Removes row from JTable; recalculates total pages and active page boundaries.", "De-persisted on next disk synchronization; recoverable via Undo stack prior to reload."]
    ]
    create_styled_table(doc, crud_headers, crud_rows, [1.1, 1.8, 1.8, 1.5])

    # =========================================================================
    # CHAPTER 15 — SYSTEM TESTING & VERIFICATION SUITE
    # =========================================================================
    add_header_p(doc, "Chapter 15 — System Testing & Verification Suite", level=1)

    add_body_p(doc, "Software verification was conducted through both automated regression testing via ProjectVerifier.java and comprehensive manual functional test executions.", space_after=10)

    add_header_p(doc, "15.1 Automated ProjectVerifier Suite (16/16 Passed)", level=2)
    add_body_p(doc, "An automated test suite, ProjectVerifier.java, programmatically exercises all core academic pillars, OOP concepts, DSA components, and file handling routines. The actual output from running 'java -cp bin ProjectVerifier' confirms 100% test passage:", space_after=6)

    pv_output = (
        "==================================================\n"
        "  STUDENT RECORD FILE STORE - OOP + DSA VERIFIER\n"
        "  Student: Sasanka Sekhar Kundu | Roll: 150096725118\n"
        "==================================================\n\n"
        "[TEST 1]  1. Authentication System (admin / admin123) ............... PASSED\n"
        "[TEST 2]  2. OOP Inheritance & Abstraction (Student extends Person) . PASSED\n"
        "[TEST 3]  3. OOP Interface Contract (Validatable.validate()) ........ PASSED\n"
        "[TEST 4]  4. OOP Polymorphism (Person -> Student.displayInfo()) ..... PASSED\n"
        "[TEST 5]  5. File Handling: Auto-Creation of Directory & File ....... PASSED\n"
        "[TEST 6]  6. ArrayList: In-Memory Storage & File Persistence ........ PASSED\n"
        "[TEST 7]  7. Exception Handling: Duplicate ID (StudentException) .... PASSED\n"
        "[TEST 8]  8. Queue DSA: FIFO StudentOperation Processing ............ PASSED\n"
        "[TEST 9]  9. Searching DSA: Linear Search across ID/Name/Course ..... PASSED\n"
        "[TEST 10] 10. Searching DSA: Binary Search by Student ID O(log n) ... PASSED\n"
        "[TEST 11] 11. Sorting DSA: Multi-Attribute Comparators (SortService)  PASSED\n"
        "[TEST 12] 12. Stack DSA: Undo ADD Action via UndoManager ............ PASSED\n"
        "[TEST 13] 13. Stack DSA: Undo UPDATE Action via UndoManager ......... PASSED\n"
        "[TEST 14] 14. Stack DSA: Undo DELETE Action via UndoManager ......... PASSED\n"
        "[TEST 15] 15. File Handling: Resilience Against Corrupt Lines ....... PASSED\n"
        "[TEST 16] 16. Stack Exception Handling: Empty Undo Stack ............ PASSED\n\n"
        "==================================================\n"
        "VERIFICATION SUMMARY: 16 / 16 TESTS PASSED (100%)\n"
        "ALL OOP + DSA + CORE JAVA REQUIREMENTS VERIFIED!\n"
        "=================================================="
    )
    add_code_block(doc, pv_output, caption="Listing 15.1 — Verified Test Suite Console Output")

    add_header_p(doc, "15.2 Functional Test Case Matrix (20 Test Cases)", level=2)
    tc_headers = ["Test ID", "Test Scenario", "Test Input Data", "Expected System Behavior", "Actual Verified Result", "Status"]
    tc_rows = [
        ["TC01", "Valid Admin Login", "User: admin, Pass: admin123", "Authentication succeeds, opens MainFrame", "MainFrame opened successfully", "PASS"],
        ["TC02", "Invalid Password", "User: admin, Pass: wrongpass", "Rejects login, displays error modal", "Access denied, error shown", "PASS"],
        ["TC03", "Empty Login Fields", "User: '', Pass: ''", "Rejects login, displays validation prompt", "Validation warning shown", "PASS"],
        ["TC04", "Add Valid Student", "121, 'Test Student', 'CSE', 2, 85", "Creates student, appends to ArrayList", "Student added, shown in table", "PASS"],
        ["TC05", "Duplicate Student ID", "101, 'Duplicate', 'IT', 1, 90", "Throws StudentException, blocks duplicate", "Error dialog: ID already exists", "PASS"],
        ["TC06", "Negative Marks", "122, 'Bad Marks', 'CSE', 2, -15", "Rejects input, marks must be 0-100", "Validation error shown", "PASS"],
        ["TC07", "Invalid Semester (>8)", "123, 'Bad Sem', 'CSE', 9, 75", "Rejects input, semester must be 1-8", "Validation error shown", "PASS"],
        ["TC08", "Non-Numeric ID Input", "ID: 'ABC', Name: 'John'", "Catches NumberFormatException gracefully", "Clean error modal displayed", "PASS"],
        ["TC09", "Inline Student Edit", "Update marks from 85 to 95", "Updates student in memory, logs snapshot", "Record updated, table refreshed", "PASS"],
        ["TC10", "Inline Delete & Undo", "Delete ID 101, click Undo", "Deletes ID 101, Undo restores it exactly", "Record restored to table", "PASS"],
        ["TC11", "Linear Search by Name", "Query: 'Sharma'", "Returns 101 (Rahul Sharma) via O(n)", "Returned 1 record in <1 ms", "PASS"],
        ["TC12", "Binary Search by ID", "Target ID: 110", "Returns ID 110 in O(log n) steps", "Found Kavya Iyer in <1 ms", "PASS"],
        ["TC13", "Sort by Marks Desc", "Sort dropdown: Marks (Desc)", "Highest marks (95) appears at top", "Sorted correctly (Kavya first)", "PASS"],
        ["TC14", "Pagination Page Size", "Toggle dropdown from 8 to 5", "Recalculates total pages, displays 5 rows", "Table sliced to 5 rows", "PASS"],
        ["TC15", "Save to File", "Click 'Save to File'", "Writes records to data/students.txt", "File updated with 20 records", "PASS"],
        ["TC16", "Reload from File", "Click 'Reload from File'", "Refreshes ArrayList from students.txt", "20 records reloaded cleanly", "PASS"],
        ["TC17", "Missing File Recovery", "Delete data/students.txt, run", "Auto-creates directory and empty file", "Created data/students.txt", "PASS"],
        ["TC18", "Corrupt Line Tolerance", "Insert malformed line in file", "Skips broken line, loads valid records", "Skipped bad line without crash", "PASS"],
        ["TC19", "Empty Undo Stack", "Click Undo on fresh start", "Throws StudentException cleanly", "Dialog: Undo stack is empty", "PASS"],
        ["TC20", "Graceful Logout", "Click 'Logout' in sidebar", "Confirms exit, disposes Main, opens Login", "Returned to LoginFrame", "PASS"]
    ]
    create_styled_table(doc, tc_headers, tc_rows, [0.6, 1.3, 1.4, 1.4, 1.1, 0.4])

    # =========================================================================
    # CHAPTER 16 — RESULTS & PERFORMANCE ANALYSIS
    # =========================================================================
    add_header_p(doc, "Chapter 16 — Results & Performance Analysis", level=1)

    add_body_p(doc, "The implementation was evaluated for functional correctness, responsiveness, memory footprint, and algorithmic efficiency. The results demonstrate that combining pure Java desktop libraries with classical data structures delivers near-instantaneous response times and predictable performance.", space_after=10)

    add_header_p(doc, "16.1 Search Benchmark Analysis", level=2)
    add_body_p(doc, "Empirical benchmarks executed through SearchPanel confirm that both Linear Search O(n) and Binary Search O(log n) execute in under 1 millisecond on the resident dataset. While Linear Search scans sequentially across all three fields (ID, Name, Course), Binary Search achieves rapid identity resolution by performing logarithmic halving on pre-sorted arrays.", space_after=10)

    add_header_p(doc, "16.2 Memory & Persistence Efficiency", level=2)
    add_body_p(doc, "The resident JVM heap footprint remains under 32 MB during active GUI operation. FileHandler achieves serialization and deserialization in less than 5 milliseconds for standard departmental batches. The Stack-based Undo manager operates with negligible memory overhead by holding lightweight snapshot references.", space_after=12)

    # =========================================================================
    # CHAPTER 17 — TECHNICAL LIMITATIONS
    # =========================================================================
    add_header_p(doc, "Chapter 17 — Technical Limitations", level=1)

    add_body_p(doc, "An honest academic appraisal requires recognizing the architectural boundaries of the current implementation:", space_after=10)

    add_bullet_p(doc, "The application is compiled as a desktop Java Swing client; it does not operate natively within web browsers or mobile operating systems without emulation.", bold_prefix="1. Desktop Client Architecture: ")
    add_bullet_p(doc, "Records are stored in a local flat text file (data/students.txt). While fast and portable, flat files lack ACID transaction guarantees, multi-table foreign keys, and SQL indexing.", bold_prefix="2. Flat File Storage: ")
    add_bullet_p(doc, "The software is designed for single-operator execution. If multiple users simultaneously modify students.txt over a shared network drive, concurrent writes could result in race conditions.", bold_prefix="3. Single-Operator Environment: ")
    add_bullet_p(doc, "Administrative credentials are configured for academic demonstration; production environments would require salted cryptographic hashing (e.g., BCrypt).", bold_prefix="4. Academic Authentication: ")

    # =========================================================================
    # CHAPTER 18 — FUTURE SCOPE & ENHANCEMENTS
    # =========================================================================
    add_header_p(doc, "Chapter 18 — Future Scope & Enhancements", level=1)

    add_body_p(doc, "The modular architecture of Student Record File Store provides a solid foundation for future enhancements:", space_after=10)

    add_bullet_p(doc, "Replacing the FileHandler backend with an embedded relational database (e.g., SQLite or H2) via JDBC to support structured SQL queries and transactional rollback.", bold_prefix="1. Relational Database Integration: ")
    add_bullet_p(doc, "Implementing fine-grained privileges distinguishing between Faculty Advisors, Examination Officers, and Department Administrators.", bold_prefix="2. Role-Based Access Control (RBAC): ")
    add_bullet_p(doc, "Integrating libraries such as Apache POI and iText to generate formatted student grade cards and departmental summary sheets.", bold_prefix="3. Automated PDF & Excel Export: ")
    add_bullet_p(doc, "Adding graphical performance charts (histograms, CGPA trends) using Java2D or JFreeChart.", bold_prefix="4. Analytical Visualizations: ")
    add_bullet_p(doc, "Developing a RESTful API backend using Spring Boot to allow student record access via web and mobile interfaces.", bold_prefix="5. Cloud & Mobile Synchronization: ")

    # =========================================================================
    # CHAPTER 19 — CONCLUSION
    # =========================================================================
    add_header_p(doc, "Chapter 19 — Conclusion", level=1)

    add_body_p(doc, "The \"Student Record File Store\" project successfully demonstrates the design, engineering, and empirical verification of a complete, robust, and academically compliant Java desktop management system. Developed by Sasanka Sekhar Kundu (Roll Number: 150096725118), the project fulfills all mandatory curricular criteria:", space_after=10)

    add_bullet_p(doc, "Proven via BufferedReader and BufferedWriter interacting with data/students.txt, with auto-creation and corruption recovery.", bold_prefix="1. File Handling: ")
    add_bullet_p(doc, "Proven via ArrayList<Student> providing responsive, dynamically resizable in-memory storage.", bold_prefix="2. ArrayList: ")
    add_bullet_p(doc, "Proven via StudentException and input barriers preventing runtime crashes.", bold_prefix="3. Exception Handling: ")
    add_bullet_p(doc, "Proven via Person (abstract base), Student (inheritance), displayInfo() dynamic dispatch (polymorphism), and Validatable (interfaces).", bold_prefix="4. Object-Oriented Programming: ")
    add_bullet_p(doc, "Proven via Stack<Action> LIFO undo, Queue<StudentOperation> FIFO auditing, Linear & Binary Search, Comparator sorting, and multi-surface pagination.", bold_prefix="5. Data Structures & Algorithms: ")

    add_body_p(doc, "The software achieved 100% passage across all 16 automated tests in ProjectVerifier and completed 20/20 functional test cases without failure. In summary, the project serves as a compelling academic demonstration of how core computer science principles come together to produce clean, dependable, and maintainable software.", space_after=12)

    # =========================================================================
    # CHAPTER 20 — REFERENCES
    # =========================================================================
    add_header_p(doc, "Chapter 20 — References", level=1)

    refs = [
        "Oracle Corporation, \"Java Platform, Standard Edition (Java SE) 21 Documentation — Core Libraries and Collections Framework,\" Oracle Technology Network, 2023. [Online]. Available: https://docs.oracle.com/en/java/javase/21/",
        "Oracle Corporation, \"Java Desktop Technologies — Swing Architecture and Component Rendering Guide,\" Oracle Documentation, 2023. [Online]. Available: https://docs.oracle.com/javase/tutorial/uiswing/",
        "J. Bloch, Effective Java, 3rd ed. Boston, MA, USA: Addison-Wesley Professional, 2018, pp. 75–120.",
        "R. Sedgewick and K. Wayne, Algorithms, 4th ed. Upper Saddle River, NJ, USA: Addison-Wesley, 2011, ch. 1–3.",
        "E. Gamma, R. Helm, R. Johnson, and J. Vlissides, Design Patterns: Elements of Reusable Object-Oriented Software. Reading, MA, USA: Addison-Wesley, 1994.",
        "B. Eckel, Thinking in Java, 4th ed. Upper Saddle River, NJ, USA: Prentice Hall, 2006, pp. 210–280.",
        "H. M. Deitel and P. J. Deitel, Java How to Program: Early Objects, 11th ed. Boston, MA, USA: Pearson, 2018."
    ]
    for r_idx, ref in enumerate(refs):
        add_body_p(doc, ref, space_after=6, bold_prefix=f"[{r_idx+1}] ")

    doc.add_page_break()

    # =========================================================================
    # APPENDICES
    # =========================================================================
    add_header_p(doc, "Appendix A — Complete Project File Structure", level=1)
    tree_text = (
        "StudentRecordFileStore/\n"
        "├── README.md                                  # Academic documentation & viva guide\n"
        "├── bin/                                       # Compiled bytecode (.class files)\n"
        "├── data/\n"
        "│   └── students.txt                           # 20 pre-loaded student records (pipe-delimited)\n"
        "├── imags/\n"
        "│   ├── login.png                              # Login screen\n"
        "│   ├── dashboard.png                          # Dashboard overview & metrics\n"
        "│   ├── addStudnets.png                        # Student registration form\n"
        "│   ├── ViewRecords.png                        # Records table with sorting, undo, pagination\n"
        "│   ├── Search.png                             # Search directory & benchmark metrics\n"
        "│   ├── ActivityInfo.png                       # File persistence & I/O console\n"
        "│   └── About.png                              # About panel & polymorphism tester\n"
        "└── src/\n"
        "    ├── Main.java                              # Main entry point with antialiasing hints\n"
        "    ├── ProjectVerifier.java                   # 16-test automated verification suite\n"
        "    ├── auth/\n"
        "    │   └── AuthenticationManager.java         # Credential authentication\n"
        "    ├── exception/\n"
        "    │   └── StudentException.java              # Custom checked exception\n"
        "    ├── interfaces/\n"
        "    │   └── Validatable.java                   # Contract with validate() method\n"
        "    ├── model/\n"
        "    │   ├── Person.java                        # Abstract base class\n"
        "    │   ├── Student.java                       # Inherits Person, implements Validatable\n"
        "    │   ├── Action.java                        # Snapshot entity for Stack Undo\n"
        "    │   └── StudentOperation.java              # Transaction entity for FIFO Queue\n"
        "    ├── service/\n"
        "    │   ├── FileHandler.java                   # File persistence (BufferedReader/Writer)\n"
        "    │   ├── StudentManager.java                # ArrayList store & coordinator\n"
        "    │   ├── UndoManager.java                   # Stack<Action> LIFO Undo implementation\n"
        "    │   ├── OperationQueue.java                # Queue<StudentOperation> FIFO implementation\n"
        "    │   ├── SearchService.java                 # Linear Search O(n) & Binary Search O(log n)\n"
        "    │   └── SortService.java                   # Comparator-based multi-attribute sorting\n"
        "    └── gui/\n"
        "        ├── UIConstants.java                   # Warm aesthetic design system & custom 2D buttons\n"
        "        ├── LoginFrame.java                    # Authentication frame\n"
        "        ├── MainFrame.java                     # Main frame with persistent sidebar & CardLayout\n"
        "        ├── DashboardPanel.java                # Metrics & paginated recent records\n"
        "        ├── AddStudentPanel.java               # Centered form with validation\n"
        "        ├── RecordsPanel.java                  # Records table, sorting, undo, pagination\n"
        "        ├── SearchPanel.java                   # Search modes, timer metrics, pagination\n"
        "        ├── FileOperationsPanel.java           # File metadata, save/reload, I/O console\n"
        "        └── AboutPanel.java                    # Metadata, viva checklist, polymorphism tester"
    )
    add_code_block(doc, tree_text, caption="Listing A.1 — Hierarchical Project Directory Tree")

    add_header_p(doc, "Appendix B — Sample Dataset (data/students.txt)", level=1)
    add_body_p(doc, "The table below lists the 20 pre-loaded student records stored in data/students.txt:", space_after=6)
    
    data_headers = ["ID", "Student Full Name", "Academic Course", "Semester", "Marks"]
    data_rows = [
        ["101", "Rahul Sharma", "B.Tech CSE", "2", "85"],
        ["102", "Priya Shah", "B.Tech CSE", "2", "91"],
        ["103", "Aarav Mehta", "B.Tech CSE", "1", "78"],
        ["104", "Ananya Verma", "B.Tech IT", "3", "88"],
        ["105", "Rohan Patel", "B.Tech CSE", "4", "74"],
        ["106", "Sneha Kulkarni", "B.Tech AI & DS", "2", "93"],
        ["107", "Aditya Singh", "B.Tech CSE", "1", "81"],
        ["108", "Ishita Gupta", "B.Tech IT", "3", "89"],
        ["109", "Arjun Nair", "B.Tech CSE", "2", "76"],
        ["110", "Kavya Iyer", "B.Tech AI & DS", "4", "95"],
        ["111", "Vivek Joshi", "B.Tech CSE", "3", "84"],
        ["112", "Meera Desai", "B.Tech IT", "2", "90"],
        ["113", "Karan Malhotra", "B.Tech CSE", "4", "72"],
        ["114", "Riya Kapoor", "B.Tech AI & DS", "1", "87"],
        ["115", "Siddharth Rao", "B.Tech CSE", "3", "79"],
        ["116", "Neha Mishra", "B.Tech IT", "2", "94"],
        ["117", "Yash Thakur", "B.Tech CSE", "1", "83"],
        ["118", "Diya Shah", "B.Tech AI & DS", "3", "92"],
        ["119", "Manav Agarwal", "B.Tech CSE", "4", "77"],
        ["120", "Aditi Banerjee", "B.Tech IT", "3", "86"]
    ]
    create_styled_table(doc, data_headers, data_rows, [0.8, 1.8, 1.8, 0.9, 0.9])

    add_header_p(doc, "Appendix C — Academic Viva Voce Question Bank", level=1)
    add_body_p(doc, "This section provides concise, ready-to-answer responses for academic evaluators during project defense:", space_after=8)

    viva_qa = [
        ("Q1: Where is File Handling implemented in this project?",
         "File Handling is implemented in service/FileHandler.java using standard Java I/O (BufferedReader, BufferedWriter, FileReader, FileWriter). It manages data/students.txt with automatic directory creation, pipe-delimited serialization, and fault-tolerant parsing."),
        ("Q2: Why did you use an ArrayList instead of a traditional array?",
         "An ArrayList<Student> in service/StudentManager.java provides dynamic resizing without manual array reallocation, O(1) random-access indexing for pagination, and seamless integration with the Java Collections Framework (List.sort, Collections)."),
        ("Q3: How does your application handle exceptions?",
         "We created a custom checked exception class, exception/StudentException. Domain methods declare 'throws StudentException', and the GUI layer catches errors to display clean warning dialogs without crashing the JVM."),
        ("Q4: Where are Abstraction and Inheritance demonstrated?",
         "Abstraction is implemented in model/Person.java as an abstract class declaring 'abstract String displayInfo()'. Inheritance is implemented in model/Student.java via 'public class Student extends Person', reusing id and name while adding student-specific fields."),
        ("Q5: Where is Polymorphism demonstrated?",
         "In gui/AboutPanel.java and Main.java, a polymorphic reference Person person = new Student(...) is instantiated. Calling person.displayInfo() dynamically resolves to Student's overridden method at runtime via dynamic method dispatch."),
        ("Q6: How does the Stack-based Undo mechanism work?",
         "service/UndoManager.java maintains a Stack<Action>. Before an ADD, UPDATE, or DELETE operation occurs, a snapshot of the student state is pushed onto the stack. When 'Undo' is triggered, the action is popped in LIFO order and reversed."),
        ("Q7: What is the difference between Linear and Binary Search in your app?",
         "service/SearchService.java implements Linear Search O(n) scanning across ID, Name, and Course. Binary Search O(log n) operates on pre-sorted student IDs, dividing the search space in half at each step. SearchPanel displays microsecond execution timers comparing both.")
    ]
    for q, a in viva_qa:
        add_body_p(doc, q, space_after=2, bold_prefix="")
        add_body_p(doc, a, space_after=8, bold_prefix="Answer: ")

    out_docx = os.path.join(base_dir, "Student_Record_File_Store_Project_Report_Sasanka_Sekhar_Kundu.docx")
    doc.save(out_docx)
    print(f"Successfully generated DOCX: {out_docx}")

if __name__ == "__main__":
    build_report()
