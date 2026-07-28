import cv2
import numpy as np

# Load the image with alpha channel
img = cv2.imread('image_0.png', cv2.IMREAD_UNCHANGED)

# Extract alpha channel
alpha = img[:, :, 3]

# Threshold to get binary image
_, binary = cv2.threshold(alpha, 127, 255, cv2.THRESH_BINARY)

# Find contours
contours, _ = cv2.findContours(binary, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

def contour_to_svg_path(contour, scale, offset_x, offset_y):
    path = []
    for i, point in enumerate(contour):
        x, y = point[0]
        x = x * scale + offset_x
        y = y * scale + offset_y
        if i == 0:
            path.append(f"M {x:.2f},{y:.2f}")
        else:
            path.append(f"L {x:.2f},{y:.2f}")
    path.append("Z")
    return " ".join(path)

# Print as SVG
print('<svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">')
for cnt in contours:
    # simplify contour slightly to reduce points
    epsilon = 0.001 * cv2.arcLength(cnt, True)
    approx = cv2.approxPolyDP(cnt, epsilon, True)
    path_str = contour_to_svg_path(approx, 1, 0, 0)
    print(f'  <path d="{path_str}" fill="lime"/>')
print('</svg>')
